import com.google.gson.Gson;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConnectToTelegramm extends TelegramLongPollingBot {

    long id;
    boolean isLocation = false;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
            Message msg = update.getMessage();
            id = update.getMessage().getChatId();
            String answer = "";

            switch (msg.getText()){

                case "/start": {
                    keywordMsg(msg, "Телефон");
                    message.setText("Ваш телефон " + msg.getContact().getPhoneNumber() + "\nВерно?");
                    if (!msg.getContact().getPhoneNumber().isEmpty())
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case "Да": {

                    break;
                }
                case "Нет, я введу сам!": {

                }

            }
        }

        else if (update.hasCallbackQuery()){
            Message msg = update.getMessage();
            SendMessage send = new SendMessage();
            try {
                execute(new SendMessage().setText(update.getCallbackQuery().getData()).setChatId(update.getCallbackQuery().getMessage().getChatId()));
                if (!isLocation){
                    keywordMsg(msg, "Геолокация");
                    isLocation = true;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        else {

isLocation = true;
            if(isLocation){
                try {

                    String res =  "Широта \"" + update.getMessage().getLocation().getLatitude().toString() + "\"\nДолгота \""+ update.getMessage().getLocation().getLongitude() + "\"";//answ1 + answ2;
                    execute(yesNoMessange(update.getMessage().getChatId(), res, "Геолокация"));
                    isLocation = false;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    execute(yesNoMessange(update.getMessage().getChatId(), update.getMessage().getContact().getPhoneNumber(), "Телефон"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public String getBotUsername() {
        return "TestMyWorksBot";
    }

    @Override
    public String getBotToken() {
        return "599490226:AAFfpMDWNdt2BPLvC3sqUwA8eYhByEdFIbM";
    }

    public void keywordMsg(Message message,String key) {
        SendMessage send = new SendMessage();
        send.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        send.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> list = new ArrayList<>();

        KeyboardButton btn1 = new KeyboardButton();
        KeyboardButton btn2 = new KeyboardButton();

        if(key.equals("Телефон")){
            send.setText("Необходим ваш номер телефона");
            btn1.setText("Отправить мой номер");
            btn2.setText("Я введу номер");
            btn1.setRequestContact(true);
        }
        else if (key.equals("Геолокация")){
            send.setText("Необходимо ваше местоположение");
            btn1.setText("Отправить моё местоположение");
            btn2.setText("Я введу адрес");
            btn1.setRequestLocation(true);
        }

        KeyboardRow row1  = new KeyboardRow();
        row1.add(btn1);
        row1.add(btn2);

        list.add(row1);

        replyKeyboardMarkup.setKeyboard(list);
        try{
            send.setChatId(message.getChatId().toString());
        }
        catch (NullPointerException e){
            send.setChatId(id);
        }

        try{
            sendMessage(send);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }

    }

    private SendMessage yesNoMessange(long chatId, String text, String type) {
        InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Да").setCallbackData("Хорошо!"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Нет, я введу сам!").setCallbackData("Жду!"));

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(type.equals("Телефон"))
            return new SendMessage().setChatId(chatId).setText("Ваш телефон: " + text + "\nВерно?").setReplyMarkup(inlineKeyboardMarkup);
        else if(type.equals("Геолокация")){
            return new SendMessage().setChatId(chatId).setText("Ваши координаты: " + text + "\nВерно?").setReplyMarkup(inlineKeyboardMarkup);
        }
        return new SendMessage().setChatId(chatId).setText("необработанная ситуация").setReplyMarkup(inlineKeyboardMarkup);
    }

}