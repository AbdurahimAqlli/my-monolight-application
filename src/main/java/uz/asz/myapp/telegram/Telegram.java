package uz.asz.myapp.telegram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.asz.myapp.domain.Student;
import uz.asz.myapp.domain.enumeration.Category;
import uz.asz.myapp.repository.StudentRepository;

@Component
public class Telegram extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "abdurahimasz_bot";
    private static final String BOT_TOKEN = "1874688369:AAFdykv-BjpOJr_I3Zc5eCOMqnz8tx2BVs8";
    private final Logger log = LoggerFactory.getLogger(Telegram.class);

    private final CheckUserService checkUserService;
    private final RegisterUserService registerUserService;
    private final StudentRepository studentRepository;

    public Telegram(CheckUserService checkUserService, RegisterUserService registerUserService, StudentRepository studentRepository) {
        this.checkUserService = checkUserService;
        this.registerUserService = registerUserService;
        this.studentRepository = studentRepository;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private StepEnums stepEnums = null;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try {
            if (update.getMessage() == null) {
                if (
                    (
                        Objects.equals(update.getCallbackQuery().getData(), "B") ||
                        Objects.equals(update.getCallbackQuery().getData(), "BC") ||
                        Objects.equals(update.getCallbackQuery().getData(), "C")
                    )
                ) {
                    Student student;
                    Optional<Student> optionalStudent = studentRepository.findFirstByChatId(
                        String.valueOf(update.getCallbackQuery().getMessage().getChatId())
                    );
                    student = optionalStudent.orElseGet(Student::new);
                    //todo
                    student.setCategory(Category.valueOf(update.getCallbackQuery().getData()));
                    studentRepository.save(student);

                    stepEnums = StepEnums.MAIN;
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    sendMessage.setText("siz muvaffigiyatli ro`yxatdan o`tdingiz siz bilan admin bog`lanadi");
                    execute(sendMessage);

                    SendMessage sendMessageToAdmin = new SendMessage();
                    sendMessageToAdmin.setChatId("1332115571");
                    sendMessageToAdmin.setText(
                        "botga yangi student registratsiya bo`ldi \n" +
                        "student ismi=" +
                        student.getName() +
                        "\n" +
                        "telegram raqami=" +
                        student.getContactNumber() +
                        "raqam\n" +
                        "telefon raqami=" +
                        student.getPhoneNumber() +
                        "\n" +
                        "categoryasi=" +
                        student.getCategory() +
                        "\n" +
                        "dars  vaqti =" +
                        student.getStudyTime()
                    );

                    execute(sendMessageToAdmin);

                    return;
                }
                if (
                    (
                        Objects.equals(update.getCallbackQuery().getData(), "?????????? #1 (?? 17:00 ???? 20:00)") ||
                        Objects.equals(update.getCallbackQuery().getData(), "?????????? #1 (?? 13:00 ???? 16:00)") ||
                        Objects.equals(update.getCallbackQuery().getData(), "?????????? #1 (?? 9:00 ???? 12:00)")
                    )
                ) {
                    Student student;
                    Optional<Student> optionalStudent = studentRepository.findFirstByChatId(
                        String.valueOf(update.getCallbackQuery().getMessage().getChatId())
                    );
                    student = optionalStudent.orElseGet(Student::new);
                    //todo
                    student.setStudyTime(update.getCallbackQuery().getData());
                    studentRepository.save(student);

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    sendMessage.setText("Qaysi category bo`yicha o`qimoqchisz!");
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("B").setCallbackData("B"));
                    rowInline.add(new InlineKeyboardButton().setText("BC").setCallbackData("BC"));
                    rowInline.add(new InlineKeyboardButton().setText("C").setCallbackData("C"));

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(Collections.singletonList(rowInline));
                    // Add it to the message
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    execute(sendMessage);
                    return;
                }
            }
            if (checkUserService.checkTeacherInDb(message.getChatId())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("salom ustoz nma gap");
                sendMessage.setChatId(message.getChatId());
                stepEnums = StepEnums.MAIN;
                execute(sendMessage);
            }
            if (checkUserService.checkInstructorInDb(message.getChatId())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("salom instruktor nma gap");
                sendMessage.setChatId(message.getChatId());
                stepEnums = StepEnums.MAIN;
                execute(sendMessage);
            }
            if (checkUserService.checkStudentInDb(message.getChatId())) {
                if (null == stepEnums) {
                    stepEnums = StepEnums.MAIN;
                }
            }
            if (stepEnums == null) {
                stepEnums = StepEnums.BEGIN;
            }
            if (stepEnums == StepEnums.BEGIN) {
                stepEnums = StepEnums.SAVE_CONTACT;
                execute(registerUserService.contactButton(message.getChatId()));
                return;
            }
            if (stepEnums == StepEnums.SAVE_CONTACT) {
                stepEnums = StepEnums.MAIN;
                Student student = new Student();
                student.setContactNumber(message.getContact().getPhoneNumber());
                student.setChatId(String.valueOf(message.getChatId()));
                student.setName(message.getContact().getFirstName());
                studentRepository.save(student);
                execute(registerUserService.branchButton(message.getChatId()));
                return;
            }

            if (Objects.equals(message.getText(), "\uD83D\uDCF0 ?????????????????????? ??????????????????")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDCC7 ?????????? ???????????????? ?? ??????????????????\n" +
                    "\uD83D\uDCC4 ?????????????????????? ?????????????? ?????????? 083\n" +
                    "\uD83D\uDCD6 ?????????? ???????????????? ???????? ??????????????\n" +
                    "\uD83D\uDC68\u200D\uD83D\uDCBC 4 ???????? (???????????? 3??4)"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCB0????????")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDCB0 ???????? ?????????? 1 ?????????? (?? 9:00 ???? 12:00)\n" +
                    "                     ?????????? 1 600 000 ??????.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 ???????? ?????????? 2 ?????????? (c 12:00 ???? 15:00)\n" +
                    "                     ?????????? 1 600 000 ??????.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 ???????? ?????????? 3 ?????????? (c 15:00 ???? 18:00)\n" +
                    "                     ?????????? 1 600 000 ??????.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 ???????? ?????????? 4 ?????????? (c 18:00 ???? 21:00) \n" +
                    "                     ?????????? 1 700 000 ??????.\n" +
                    "------------------------------------------------\n" +
                    "???????? ?????????????????? ???????????????? ?????? ?????????????? ?????????????????? ?? ?????????????????? ?? ?????????????? ?????????????? ??????????????????\n" +
                    "\uD83D\uDCC6 ?????? ?????????????? ?????????? ???????????????????? ??????????????. ???????????? ?????????? 500 000\n" +
                    "\uD83D\uDCB3 ?????????? ???????????? ?????????? (????????????????, ????????????????, ????????????????????????"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "??? ???????????? ??????????????")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDD58 ?????????? #1 (?? 9:00 ???? 12:00)\n" +
                    "\uD83D\uDD5B ?????????? #2 (?? 12:00 ???? 15:00)\n" +
                    "\uD83D\uDD52 ?????????? #3 (?? 15:00 ???? 18:00)\n" +
                    "\uD83D\uDD61 ?????????? #4 (?? 18:00 ???? 21:00)\n" +
                    "\uD83D\uDCC6 ???????? ???????????? 56 ???????? (2,5 ????????????).\n" +
                    "\uD83D\uDCDD ?????????????????????????? ?????????????? ???????????????????? ?? ???????????? ???????????? 5 ???????? ?? ???????????? ?? ???????????????????????? ???? ??????????????.\n" +
                    "\uD83D\uDE98 ???????????????????????? ?????????????? ???? ???????????????? ???????????????????? 3 ?????? ?? ???????????? ?? ???????????????????????? ?? ????????????????."
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "?????? ???????? ????????????????")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "?????? ??????.: 93 575 80 30\n" +
                    "\uD83D\uDCF1 Telegram: t.me/abdurahimASZ\n" +
                    "\uD83D\uDEC2 Telegram bot: t.me/abdurahimasz_bot\n" +
                    "\uD83C\uDF10 ????????: www.avtolux.uz \n" +
                    "\uD83D\uDCE7 ??mail: dabdalaji@gmail.com \n" +
                    "\uD83D\uDCF7 Instagram: https://www.instagram.com/abdurahim_asz/\n" +
                    "\uD83D\uDCBB Facebook: https://www.facebook.com/abdurahim.otkirbekov.7/"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCF7  ??????????????????????")) {
                SendPhoto sendMessage = new SendPhoto();
                sendMessage.setCaption("avtomaktab");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setPhoto(
                    "avtomaktab",
                    new FileInputStream(new File("C:\\Users\\Lenovo\\Desktop\\salom\\avtomaktab\\unnamed.jpg"))
                );
                execute(sendMessage);

                sendMessage.setPhoto(
                    "avtomaktab",
                    new FileInputStream(new File("C:\\Users\\Lenovo\\Desktop\\salom\\avtomaktab\\377015996.jpg"))
                );
                execute(sendMessage);

                sendMessage.setPhoto(
                    "avtomaktab",
                    new FileInputStream(new File("C:\\Users\\Lenovo\\Desktop\\salom\\avtomaktab\\1-408x306.jpg"))
                );
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCCD?????? ??????????")) {
                SendLocation sendLocation = new SendLocation().setLatitude(41.383346F);
                sendLocation.setLongitude(69.2857343F);
                sendLocation.setChatId(message.getChatId());
                execute(sendLocation);
                return;
            }

            if (Objects.equals(message.getText(), "\uD83D\uDCDD ?????????????????? ???????????? ???? ????????????????")) {
                Student student;

                SendMessage sendMessage = new SendMessage();

                Optional<Student> optionalStudent = studentRepository.findFirstByChatId(String.valueOf(message.getChatId()));

                student = optionalStudent.orElseGet(Student::new);
                stepEnums = StepEnums.REGISTER_FIO;

                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("??? ?????????????????? ?????? ??.??.??.");
                student.setChatId(String.valueOf(message.getChatId()));
                studentRepository.save(student);

                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FIO) {
                Student student;
                Optional<Student> optionalStudent = studentRepository.findFirstByChatId(String.valueOf(message.getChatId()));
                student = optionalStudent.orElseGet(Student::new);

                stepEnums = StepEnums.REGISTER_PHONE_NUMBER;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                student.setName(message.getForwardSenderName() + message.getText());
                studentRepository.save(student);

                sendMessage.setText("??? ?????????????????? ???????? ?????????? ????????????????");
                execute(sendMessage);

                return;
            }
            if (stepEnums == StepEnums.REGISTER_PHONE_NUMBER) {
                Student student;
                Optional<Student> optionalStudent = studentRepository.findFirstByChatId(String.valueOf(message.getChatId()));
                student = optionalStudent.orElseGet(Student::new);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("??? ?? ?????????? ?????????? ???? ???????????? ???? ???????????????????");
                student.setPhoneNumber(message.getText());
                studentRepository.save(student);
                List<List<InlineKeyboardButton>> list = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
                rowInline1.add(
                    new InlineKeyboardButton().setText("?????????? #1 (?? 9:00 ???? 12:00)").setCallbackData("?????????? #1 (?? 9:00 ???? 12:00)")
                );
                rowInline2.add(
                    new InlineKeyboardButton().setText("?????????? #1 (?? 13:00 ???? 16:00)").setCallbackData("?????????? #1 (?? 13:00 ???? 16:00)")
                );
                rowInline3.add(
                    new InlineKeyboardButton().setText("?????????? #1 (?? 17:00 ???? 20:00)").setCallbackData("?????????? #1 (?? 17:00 ???? 20:00)")
                );
                list.add(rowInline1);
                list.add(rowInline2);
                list.add(rowInline3);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(list);
                // Add it to the message
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                execute(sendMessage);
                return;
            }

            if (stepEnums == StepEnums.MAIN) {
                execute(registerUserService.branchButton(message.getChatId()));
            }
        } catch (TelegramApiException e) {
            log.warn("TelegramApiException ex {}", e.getMessage());
        } catch (FileNotFoundException e) {
            log.warn("FileNotFoundException handle exception {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
}
