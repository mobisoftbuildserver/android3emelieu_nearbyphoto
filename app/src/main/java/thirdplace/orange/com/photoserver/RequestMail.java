package thirdplace.orange.com.photoserver;

import java.io.File;

public class RequestMail {

        private String sender;
        private String email;
        private File file;

        public RequestMail(String aSender,String aEmail,File aFile) {
            this.sender = aSender;
            this.email = aEmail;
            this.file = aFile;
        }

        public String getSender() {
            return sender;
        }

        public String getEmail() {
            return email;
        }

        public File getFile() {
            return file;
        }
    }