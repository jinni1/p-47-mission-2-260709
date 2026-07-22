package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {
    Scanner sc = new Scanner(System.in);
    private final Path DB_PATH = Paths.get("db", "wiseSaying");

    public void run() throws IOException {
        // 폴더 생성
        Files.createDirectories(DB_PATH);

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();
            int id = -1;

            if (command.startsWith("삭제") || command.startsWith("수정")) {
                String[] parts = command.split("[?=]");

                if (parts.length != 3) {
                    System.out.println("명령 형식이 올바르지 않습니다.");
                    return;
                }

                command = parts[0];

                // id 값이 올바르지 않은 경우 예외처리
                try {
                    id = Integer.parseInt(parts[2]);

                    if (id < 1) {
                        System.out.println("id는 1 이상의 자연수여야 합니다.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("id는 숫자여야 합니다.");
                    return;
                }
            }

            switch (command) {
                case "종료":
                    return;
                case "등록":
                    add();
                    break;
                case "목록":
                    getWiseList();
                    break;
                case "삭제":
                    delete(id);
                    break;
                case "수정":
                    update(id);
                    break;
                case "빌드":
                    build();
            }
        }
    }

    public void add() throws IOException {
        System.out.print("명언 : ");
        String content = sc.nextLine();

        System.out.print("작가 : ");
        String author = sc.nextLine();

        // 다음 id 값을 읽어옮
        int lastId = getlastID();

        // 새로운 명언 생성
        Wise wise = new Wise(++lastId, content, author);

        // json 파일로 명언 저장
        Path filePath = DB_PATH.resolve(wise.getId() + ".json");

        String json = wiseToJson(wise);

        Files.writeString(filePath, json);

        System.out.println(wise.getId() + "번 명언이 등록되었습니다.");

        // 가장 마지막 id 업데이트
        Path filePathId = DB_PATH.resolve("lastId.txt");
        FileWriter fwId = new FileWriter(filePathId.toFile(), false);
        fwId.write(wise.getId() + "\n");
        fwId.close();

    }

    public String wiseToJson(Wise wise) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"id\": ");
        sb.append(wise.getId());
        sb.append(",\n");
        sb.append("    \"content\": \"");
        sb.append(wise.getContent());
        sb.append("\",\n");
        sb.append("    \"author\": \"");
        sb.append(wise.getAuthor());
        sb.append("\"\n");
        sb.append("}");

        return sb.toString();
    }

    public int getlastID()  throws IOException {
        // lastId.txt 파일이 존재하는지 확인
        Path filePathId = DB_PATH.resolve("lastId.txt");

        // 파일이 존재하면 파일에서 값을 읽어옮
        if (Files.exists(filePathId)) {
            String id = Files.readString(filePathId);

            return Integer.parseInt(id.trim());
        } else { // 파일이 존재하지 않으면 1을 반환
            return 0;
        }
    }



    public void getWiseList() throws IOException {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        int lastId = getlastID();
        for (int i = lastId; i >= 1; i--) {
            Wise wise = findById(i);
            if (wise != null) {
                System.out.println(wise.getId() + " / " + wise.getAuthor() + " / " + wise.getContent());
            }
        }

    }

    public Wise findById(int id) throws IOException {
        Path filePath = DB_PATH.resolve(id + ".json");

        if (Files.exists(filePath)) {
            String json = Files.readString(filePath);
            String[] rst1 = json.split("\"id\": ");
            String[] rst2 = rst1[1].split(",\n\\s*\"content\":\\s\"");
            int wiseId = Integer.parseInt(rst2[0].trim());

            String[] rst3 = rst2[1].split("\",\n\\s*\"author\":\\s\"");
            String content = rst3[0];

            String[] rst4 = rst3[1].split("\"\n}");
            String author = rst4[0];

            return new Wise(wiseId, content, author);
        } else {
            return null;
        }

        /* 스트림을 이용해서 인덱스를 찾는 방법
            int index = IntStream.range(0, wiseArray.size())
            .filter((int i) -> wiseArray.get(i).getId() == id)
            .findFirst()
            .orElse(-1);
         */
    }

    public void delete(int id) throws IOException {
        if (Files.exists(DB_PATH.resolve(id + ".json"))) {
            Files.delete(DB_PATH.resolve(id + ".json"));


            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }

        /* removeIf 사용하는 방법
            boolean rst = wiseArray.removeIf(wise -> wise.getId() == id);
         */
    }

    public void update(int id) throws IOException {
        Wise wise = findById(id);
        if (wise != null) {
            System.out.println("명언(기존) : " + wise.getContent());
            System.out.print("명언 : ");
            String newContent = sc.nextLine();

            System.out.println("작가(기존) : " + wise.getAuthor());
            System.out.print("작가 : ");
            String newAuthor = sc.nextLine();

            // wise 객체 수정
            wise.updateWise(newContent, newAuthor);


            // json 파일로 명언 저장
            Path filePath = DB_PATH.resolve(wise.getId() + ".json");

            String json = wiseToJson(wise);

            Files.writeString(filePath, json);

        }
        else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void build() throws IOException {
        Path filePath = DB_PATH.resolve("data.json");

        int lastId = getlastID();

        StringBuilder sb = new StringBuilder();

        sb.append("[\n");
        for (int i = 1; i <= lastId; i++) {
            Wise wise = findById(i);
            if (wise != null) {
                sb.append("    {\n");
                sb.append("        \"id\": ");
                sb.append(wise.getId());
                sb.append(",\n");
                sb.append("        \"content\": \"");
                sb.append(wise.getContent());
                sb.append("\",\n");
                sb.append("        \"author\": \"");
                sb.append(wise.getAuthor());
                sb.append("\"\n");
                sb.append("    }");

                if(i != lastId) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
        }
        sb.append("]");

        String dataJson = sb.toString();

        Files.writeString(filePath, dataJson);
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}