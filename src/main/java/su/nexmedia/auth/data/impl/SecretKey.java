package su.nexmedia.auth.data.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.api.encryption.EncryptionType;
import su.nightexpress.nightcore.util.Colorizer;

public class SecretKey {

    private String question;
    private String answer;

    public SecretKey(@NotNull String question, @NotNull String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    @NotNull
    public static SecretKey empty() {
        return new SecretKey("", "");
    }

    public boolean isSet() {
        return !this.getQuestion().isEmpty() && !this.getAnswer().isEmpty();
    }

    public void reset() {
        this.setQuestion("");
        this.setAnswer("");
    }

    @NotNull
    public String getQuestion() {
        return question;
    }

    public void setQuestion(@NotNull String question) {
        this.question = Colorizer.restrip(question);
    }

    @NotNull
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(@NotNull String answer) {
        this.answer = EncryptionType.SHA256.getEncrypter().encrypt(Colorizer.restrip(answer));
    }

    public boolean isAnswer(@NotNull String input) {
        return this.getAnswer().equals(EncryptionType.SHA256.getEncrypter().encrypt(Colorizer.restrip(input)));
    }
}
