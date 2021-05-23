package uz.asz.myapp.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.asz.myapp.domain.Group;
import uz.asz.myapp.domain.Student;
import uz.asz.myapp.domain.enumeration.Category;

//@Component todo shu annotationni yoqsam telegram bot ishlami qovotti agar yoqmasam  CheckUserService ,
// todo RegisterUserService servicelarni  chaqiromayamman init bo`mayabdi null bo`lib qolib ketvotti
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
        SendMessage sendMessage = new SendMessage();
        try {
            if (checkUserService.checkTeacherInDb(message.getChatId())) {
                sendMessage.setText("salom ustoz che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
            }
            if (checkUserService.checkInstructorInDb(message.getChatId())) {
                sendMessage.setText("salom instruktor che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
            }
            if (checkUserService.checkStudentInDb(message.getChatId())) {
                sendMessage.setText("salom student che gap");
                sendMessage.setChatId(message.getChatId());
                execute(sendMessage);
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
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_1;
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("✅ Отправьте нам Ф.И.О.");
                student.setChatId(String.valueOf(message.getChatId()));
                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_1) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_2;
                sendMessage.setChatId(message.getChatId());
                student.setFirstName(message.getForwardSenderName());
                student.setLastName(message.getForwardSenderName());

                sendMessage.setText("✅ Отправьте свой номер телефона");
                execute(sendMessage);

                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_2) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_3;
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("✅ В какой смене вы хотели бы обучаться?");
                student.setPhoneNumber(message.getText());
                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_3) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_4;
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Qaysi category bo`yicha o`qimoqchisz!");
                execute(sendMessage);
                return;
            }
            if (stepEnums == StepEnums.REGISTER_FOR_TEACHING_4) {
                stepEnums = StepEnums.REGISTER_FOR_TEACHING_5;
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
                sendMessage.setReplyMarkup(keyboardMarkup);
                execute(sendMessage);
                return;
            }

            if (stepEnums == StepEnums.MAIN) {
                sendMessage.setChatId(message.getChatId());
                execute(registerUserService.branchButton(message.getChatId()));
            }
        } catch (TelegramApiException e) {
            log.warn("TelegramApiException ex {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
}
