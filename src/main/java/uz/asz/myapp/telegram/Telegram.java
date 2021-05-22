package uz.asz.myapp.telegram;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Telegram extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "abdurahimasz_bot";
    private static final String BOT_TOKEN = "1874688369:AAFdykv-BjpOJr_I3Zc5eCOMqnz8tx2BVs8";
    private final Logger log = LoggerFactory.getLogger(Telegram.class);

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message.getText());
        sendMessage.setChatId(message.getChatId());
        try {
            execute(sendMessage);
            contactButton(message.getChatId());
        } catch (TelegramApiException e) {
            log.warn("TelegramApiException ex {}", e.getMessage());
        }
    }

    private void contactButton(long chat_id) {
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
}
