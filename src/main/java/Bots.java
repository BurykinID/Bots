import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import sun.plugin2.message.Message;

import java.util.ArrayList;

public class Bots {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new ConnectToTelegramm());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

}

class Answer {

    boolean ok;
    ArrayList<Date> result;

    public Answer(boolean ok, ArrayList<Date> result){
        this.ok = ok;
        this.result = result;
    }

}

class Date {
    long update_id;
    message message;

    public Date(long update_id, message message){
        this.update_id = update_id;
        this.message = message;
    }

}

class message {
    int message_id;
    from from;
    chat chat;
    String date;
    reply_to_message reply_to_message;
    location location;

    public message(int message_id, from from, chat chat, String date, reply_to_message reply_to_message, location location){

        this.message_id = message_id;
        this.from = from;
        this.chat = chat;
        this.date = date;
        this.reply_to_message = reply_to_message;
        this.location = location;

    }

}

class from {

    double id;
    boolean is_bot;
    String first_name;
    String last_name;
    String language_code;

    public from(double id, boolean is_bot, String first_name, String last_name, String language_code){
        this.id = id;
        this.is_bot = is_bot;
        this.first_name = first_name;
        this.last_name = last_name;
        this.language_code = language_code;
    }

}

class chat {

    double id;
    String first_name;
    String last_name;
    String type;

    public chat(double id, String first_name, String last_name, String type){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.type = type;
    }

}

class reply_to_message {
    int message_id;
    from from;
    chat chat;
    String date;
    String text;

    public reply_to_message(int message_id, from from, chat chat, String date, String text){
        this.message_id = message_id;
        this.from = from;
        this.chat = chat;
        this.date = date;
        this.text = text;
    }

}

class location {
    double latitude;
    double longitude;

    public location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

}