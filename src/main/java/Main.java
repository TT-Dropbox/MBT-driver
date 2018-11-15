import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final int SSH_PORT = 22;
    private static final String USERNAME = "reg";

    private static final String VM1_IP = "192.168.1.140";
    private static final String VM2_IP = "192.168.1.14";
    private static final String VMOBS_IP = "192.168.1.191";

    private static final String VM1_PRK = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEogIBAAKCAQEAvJ2Q3INpFaPbI8WBL+Pz22L52nCfYafkKE+HIs+KAIX+lz1O\n" +
            "GiWbLRnmi9pgNz/kYan/E5gKyX4OoKvTpA4OLOvC0HafosYjEN0QsC5CfwFurcEv\n" +
            "f6FdR8T7MxFTmEZGm1O4rMSFhc4uhlWbSGO20UAt3rcOqCogpfEkGE0g9d/fiSBR\n" +
            "q0FdlseDXotUTL108llL5U8J0s6x4ZBFAmkfAcAUaul0bSE83pu+dii3YbYX4QYm\n" +
            "mOfwNjOUQL5zr7WdnZ34/fXKUIvA8EdKNDCyitSYfHCzESwjWDlkTdNNm53gKV9D\n" +
            "HN8CWoNzv4nsv9uuSXi02qJhNdhWKVnqIxNs7QIDAQABAoIBABBLlJ5YhBWoiGA4\n" +
            "ygZ3LTRBAtEKddqcBKwu4r/xKj0NVod+dmbv5M4qtK5UeYeMb5C4fR8WPahWybC7\n" +
            "55b+196tp7EW7ofDknf8gVqXRItwdj1wuUfPix2OOR2aW5yGRcdZnlTezaJXw9E7\n" +
            "SCUi6EJ/xva83flNWnC7uqCW2UgknyM901wghrIhjtFD/pFO6OA8W/6TrbBWSjfr\n" +
            "apGGmfO/RUtyNDCkgb0bkMHAdPyHUm9weIBHQdBcYb5NDPale6iapC6NyNl12I34\n" +
            "es6kk/9x11YTZyugnVa0/Qn+MaXkGNc6oo31xZaCRG7eKEg+iobBZaw5XHHPJI/h\n" +
            "5P4ehcECgYEA8zP59UDhLFdYlScOjvrhPbOOWcOb1QRmZcSTwBX89Hj9OfgxYGLJ\n" +
            "Fx+amaUtcRqWiqX5N5ykWCFxAk3+fDLMIrVZ+bCkl7lpGn2XVLBD0m51hgvx3aUf\n" +
            "CxVyyN//IoFaPgZXNQrOzvsPdcjMHhJ0JaILoGjjirBSubnOl2KJd8kCgYEAxopH\n" +
            "EYs0wADBcqfaGXLjzaoqJ82eCMR2G4tTCURD2+/iLzyTYZa/IITM+Su3PV8Sk7T/\n" +
            "AqoquOxrqibj/XhHsRHCG6RmeZwq+AWVylSo2FVB60nCU5y1+DCVya74oHk5eO6y\n" +
            "nGBb0YSI7bnbamANQJ17h2UJ8/Kvu8SGfbRaZgUCgYAlI5LQORApzUzhug0nGHi9\n" +
            "C2Z5nr11Ui6w68wEUVdHnhJNf/FKXsuGlHTvcaH227CRi7b7HxiZvMGdMHvkS32Q\n" +
            "71DGKIt5//5k5Tju3dv9jpCz368Xwddzwdq9gjdb9ZTqU92NZBEg2oYJ3pgNH5RN\n" +
            "CunrnRjiXPrFEfLSe2GywQKBgH1+kBd9x1UJ5T0k7g4h+e5Y/hZ0uMzP6dvVQCJe\n" +
            "XQpbYbv77SjFUYArkSh6wnNvcd2djYdQqnqDLja6KGhiK+a309sHGqMJ3HszhJAE\n" +
            "3UkI8wbXc5bIOplrlHKMP6mnlnVjY9DGc6NGxAqH7TIbDqVmJdvaOOVS5FOqkFxK\n" +
            "sM1FAoGAQMY9ITHa1yaowTpWK9UAdkubeTTE4SbrU6RrpWT90akEDqS6fxo2udLP\n" +
            "lVsFI0E2y+A+sJmtbwXcEaaZuD/b+uCnP0z9Ttp42bKZlrTyGIAdZ51SY4m0CVIV\n" +
            "MUJsHN9byhViaaOK+i/1UmURvkx5zblF8FDrrBzjTsn/4Agtt28=\n" +
            "-----END RSA PRIVATE KEY-----\n";

    private static Shell shellVM1;
    private static Shell shellVM2;
    private static Shell shellVMOBS;

    public static void main(String[] args) throws IOException {
        shellVM1 = new SSH(VM1_IP, SSH_PORT, USERNAME, VM1_PRK);
        shellVM2 = new SSH(VM2_IP, SSH_PORT, USERNAME, VM1_PRK);
        shellVMOBS = new SSH(VMOBS_IP, SSH_PORT, USERNAME, VM1_PRK);

        Scanner input = new Scanner(System.in);

        Main driver = new Main();
        while (input.hasNextLine()) {
            String nextLine = input.nextLine();

            try {
                driver.parseInput(nextLine);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            if ("exit".equals(nextLine)) break;
        }
    }

    private void parseInput(final String input) throws IOException, InterruptedException {
        if (input.startsWith("?write")) {

            String[] values = input.substring(input.indexOf('(') + 1, input.indexOf(')')).split(",");
            String val1 = values[0];
            String val2 = values[1];

            if (val1.equals(val2)) {
                processAsEqualInputs(val1, val2);
            } else {
                processAsDifferentInputs(val1, val2);
            }

        } else if (input.startsWith("?stabilize")) {
            clean();
        }
    }

    private void processAsEqualInputs(String val1, String val2) throws InterruptedException, IOException {
        writeConcurrently(val1, val2);
        Thread.sleep(10000);

        createResponse("eq", "");

        if (conflictFileExists()) {
            String conflictVal = readConflictFile();
            createResponse("conflict", conflictVal);
        }
    }

    private void processAsDifferentInputs(String val1, String val2) throws InterruptedException, IOException {
        writeConcurrently(val1, val2);
        Thread.sleep(10000);

        String val = readFile();
        if (val.equals("a@content")) {
            createResponse("read2", val);
        } else {
            createResponse("read1", val);
        }

        String conflictVal = readConflictFile();
        createResponse("conflict", conflictVal);
    }

    private void createResponse(final String name, final String val) {
        System.out.println("!" + name + "(" + val + ")");
    }

    private void writeConcurrently(String val1, String val2) {
        Shell.Plain plain1 = new Shell.Plain(shellVM1);
        Shell.Plain plain2 = new Shell.Plain(shellVM2);

        MyThread t1 = new MyThread(plain1, "echo -n '" + val1 + "' > Dropbox/test1.txt");
        MyThread t2 = new MyThread(plain2, "echo -n '" + val2 + "' > Dropbox/test1.txt");

        t1.start();
        t2.start();
    }

    private String readFile() throws IOException {
        Shell.Plain plainObs = new Shell.Plain(shellVMOBS);
        return plainObs.exec("cat Dropbox/test1.txt");
    }

    private String readConflictFile() throws IOException {
        Shell.Plain plainObs = new Shell.Plain(shellVMOBS);

        String fileName = getConflictFileName();
        return plainObs.exec("cat Dropbox/" + fileName);
    }

    private boolean conflictFileExists() throws IOException {
        Shell.Plain plainObs = new Shell.Plain(shellVMOBS);

        String fileName = getConflictFileName();
        return plainObs.exec("[ -f Dropbox/" + fileName + " ] && echo \"exists\"").equals("exists\n");
    }

    private String getConflictFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "test1\\ \\(ubuntu\\'s\\ conflicted\\ copy\\ " + LocalDate.now().format(formatter) + "\\).txt";
    }

    private void clean() throws IOException {
        Shell.Plain plainObs = new Shell.Plain(shellVMOBS);

        String fileName = getConflictFileName();
        plainObs.exec("rm Dropbox/" + fileName);
    }

    class MyThread extends Thread {

        String[] commands;
        Shell.Plain plain;

        public MyThread(Shell.Plain plain, String... commands) {
            this.commands = commands;
            this.plain = plain;
        }

        @Override
        public void run() {
            try {
                for (String command : commands) {
                    plain.exec(command);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
