package uz.asz.myapp.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.asz.myapp.domain.Student;
import uz.asz.myapp.repository.StudentRepository;

@Service
public class RegisterUserService {

    private final Logger log = LoggerFactory.getLogger(RegisterUserService.class);
    private final StudentRepository studentRepository;

    public RegisterUserService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public boolean registerStudent(long chatId) {
        Optional<Student> optionalStudent = studentRepository.findFirstByChatId(String.valueOf(chatId));
        log.info("optionalStudent {}", optionalStudent);
        return optionalStudent.isPresent();
    }

    public SendMessage contactButton(long chat_id) {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setOneTimeKeyboard(true);
        keyboard.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Telefon raqaqmini yuborish");

        button.setRequestContact(true);
        row.add(button);
        keyboardRows.add(row);
        keyboard.setKeyboard(keyboardRows);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText("Telefon raqamingizni jo'nating!");
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public SendMessage branchButton(long chat_id) {
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(chat_id)
            .setText("Here is your keyboard");
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow rowBase = new KeyboardRow();

        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        rowBase.add("\uD83D\uDCDD Отправить заявку на обучение");
        row.add("\uD83D\uDCF0 Необходимые документы");
        row.add("\uD83D\uDCF7  Фотогалерея");
        row.add("\uD83D\uDCB0Цена");
        // Add the first row to the keyboard
        keyboard.add(rowBase);

        keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("⏰ График занятий");
        row.add("☎️ Наши контакты");
        row.add("\uD83D\uDCCDНаш адрес");
        // Add the second row to the keyboard

        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        keyboardMarkup.setOneTimeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);
        return message;
    }
}
