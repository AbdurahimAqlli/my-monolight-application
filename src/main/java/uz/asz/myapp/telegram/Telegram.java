package uz.asz.myapp.telegram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.asz.myapp.domain.Student;

@Component
public class Telegram extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "abdurahimasz_bot";
    private static final String BOT_TOKEN = "1874688369:AAFdykv-BjpOJr_I3Zc5eCOMqnz8tx2BVs8";
    private final Logger log = LoggerFactory.getLogger(Telegram.class);

    private final CheckUserService checkUserService;
    private final RegisterUserService registerUserService;

    public Telegram(CheckUserService checkUserService, RegisterUserService registerUserService) {
        this.checkUserService = checkUserService;
        this.registerUserService = registerUserService;
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
            if (checkUserService.checkTeacherInDb(message.getChatId())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("salom ustoz che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (checkUserService.checkInstructorInDb(message.getChatId())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("salom instruktor che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (checkUserService.checkStudentInDb(message.getChatId())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("salom student che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (stepEnums == null) {
                stepEnums = StepEnums.BEGIN;
            }
            if (stepEnums == StepEnums.BEGIN) {
                stepEnums = StepEnums.MAIN;
                execute(registerUserService.contactButton(message.getChatId()));
                return;
            }

            Student student = new Student();
            if (Objects.equals(message.getText(), "\uD83D\uDCDD Отправить заявку на обучение")) {
                SendMessage sendMessage = new SendMessage();
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_1;
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("✅ Отправьте нам Ф.И.О.");
                student.setChatId(String.valueOf(message.getChatId()));
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCF0 Необходимые документы")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDCC7 Копия паспорта с пропиской\n" +
                    "\uD83D\uDCC4 Медицинская справка формы 083\n" +
                    "\uD83D\uDCD6 Копия атестата либо диплома\n" +
                    "\uD83D\uDC68\u200D\uD83D\uDCBC 4 фото (размер 3х4)"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCB0Цена")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDCB0 Цена курса 1 смены (с 9:00 до 12:00)\n" +
                    "                     равна 1 600 000 сум.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 Цена курса 2 смены (c 12:00 до 15:00)\n" +
                    "                     равна 1 600 000 сум.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 Цена курса 3 смены (c 15:00 до 18:00)\n" +
                    "                     равна 1 600 000 сум.\n" +
                    "------------------------------------------------\n" +
                    "\uD83D\uDCB0 Цена курса 4 смены (c 18:00 до 21:00) \n" +
                    "                     равна 1 700 000 сум.\n" +
                    "------------------------------------------------\n" +
                    "❗️В стоимость включены все расходы связанные с обучением и НИКАКИХ СКРЫТЫХ ДОПЛАТ❗️\n" +
                    "\uD83D\uDCC6 При желании можно оплачивать частями. Первый взнос 500 000\n" +
                    "\uD83D\uDCB3 Форма оплаты любая (терминал, наличные, перечисление"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "⏰ График занятий")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "\uD83D\uDD58 Смена #1 (с 9:00 до 12:00)\n" +
                    "\uD83D\uDD5B Смена #2 (с 12:00 до 15:00)\n" +
                    "\uD83D\uDD52 Смена #3 (с 15:00 до 18:00)\n" +
                    "\uD83D\uDD61 Смена #4 (с 18:00 до 21:00)\n" +
                    "\uD83D\uDCC6 Курс длится 56 дней (2,5 месяца).\n" +
                    "\uD83D\uDCDD Теоретические занятия проводятся в онлайн режиме 5 дней в неделю с понедельника по пятницу.\n" +
                    "\uD83D\uDE98 Практические занятия по вождению проводятся 3 дня в неделю в соответствии с графиком."
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "☎️ Наши контакты")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(
                    "☎️ Тел.: 93 575 80 30\n" +
                    "\uD83D\uDCF1 Telegram: t.me/intercruz\n" +
                    "\uD83D\uDEC2 Telegram bot: t.me/intercruzbot\n" +
                    "\uD83C\uDF10 Сайт: www.intercruz.uz \n" +
                    "\uD83D\uDCE7 Еmail: support@intercruz.uz\n" +
                    "\uD83D\uDCF7 Instagram: instagram.com/intercruz.uz\n" +
                    "\uD83D\uDCBB Facebook: fb.com/driving.school.Intercruz/"
                );
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCF7  Фотогалерея")) {
                SendPhoto sendMessage = new SendPhoto()
                    .setPhoto(
                        "my angel",
                        new FileInputStream(new File("C:\\Users\\Lenovo\\Desktop\\salom\\shaxsiy\\200095900609_184873.jpg"))
                    );
                sendMessage.setCaption("my angel");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
                return;
            }
            if (Objects.equals(message.getText(), "\uD83D\uDCCDНаш адрес")) {
                SendLocation sendLocation = new SendLocation().setLatitude(41.383346F);
                sendLocation.setLongitude(69.2857343F);
                sendLocation.setChatId(message.getChatId());
                execute(sendLocation);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_1) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_2;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                student.setFirstName(message.getForwardSenderName());
                student.setLastName(message.getForwardSenderName());

                sendMessage.setText("✅ Отправьте свой номер телефона");
                execute(sendMessage);

                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_2) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_3;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("✅ В какой смене вы хотели бы обучаться?");
                student.setPhoneNumber(message.getText());
                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_3) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_4;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Qaysi category bo`yicha o`qimoqchisz!");
                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_4) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_5;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row1 = new KeyboardRow();
                row1.add("B");
                KeyboardRow row2 = new KeyboardRow();
                row2.add("BC");

                KeyboardRow row3 = new KeyboardRow();
                row3.add("C");
                keyboard.add(row1);
                keyboard.add(row2);
                keyboard.add(row3);
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                keyboardMarkup.setSelective(true);
                keyboardMarkup.setOneTimeKeyboard(true);
                keyboardMarkup.setOneTimeKeyboard(true);

                sendMessage.setReplyMarkup(keyboardMarkup);
                execute(sendMessage);
                return;
            }

            if (stepEnums == StepEnums.MAIN) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
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
