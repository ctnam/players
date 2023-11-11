package players;

public class Message {
    private final String title, contents, signature;

    public static class Compose {
        private final String title, contents;
        private String signature;

        public Compose (String title, String contents) {
            this.title = title;
            this.contents = contents;
        }
        public Compose signature (String value) {signature = value; return this;}

        public Message finish () {
            return new Message(this);
        }
    }

    private Message (Compose compose) {
        title = compose.title;
        contents = compose.contents;
        signature = compose.signature;
    }

    @Override
    public String toString () {
        return String.format("Title: %s, Contents: %s, Signature: %s", title, contents, signature);
    }
}
