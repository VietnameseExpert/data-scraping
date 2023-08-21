import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String URL = "https://www.leagueoflegends.com/en-us/champions/";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String content = response.body();
//        System.out.println(content);

        ArrayList<String> charName = s(content, "style__Text-sc-n3ovyt-3 kThhiV");
        System.out.println(charName);
        System.out.println(charName.size());
    }

    public static ArrayList<String> s(String content, String value) {
        ArrayList<String> CharacterName = new ArrayList<>();

        //--------------------------------findClass------------------------------//
        int index = 0;
        while (index < content.length()) {

            boolean same = true;
            if (String.valueOf(content.charAt(index)).equals("c")) {
                String[] comparedString = new String[5];
                String[] wordInClass = {"c", "l", "a", "s", "s"};

                if (content.length() - index >= wordInClass.length) {

                    for (int i = index; i < index + wordInClass.length; i++) {
                        comparedString[i - index] = String.valueOf(content.charAt(i));

                        int nullPos = 0;
                        while (comparedString[nullPos] != null) {
                            if (nullPos + 1 < comparedString.length) {
                                nullPos++;
                            } else {
                                break;
                            }
                        }

                        for (int comparedIndex = 0; comparedIndex < nullPos; comparedIndex++) {
                            if (!Objects.equals(comparedString[comparedIndex], wordInClass[comparedIndex])) {
                                same = false;
                                break;
                            }
                        }
                    }
                }   else {
                    same = false;
                }
//-----------------------------------------------------getCLassName-----------------------------------------------------
                // if class
                // info now:
                // 1. index
                //iterate from end class (index + wordInClass.length) -> = sign -> any char not " " -> syntax error -->
                if (same) {
                    int endClass = index + wordInClass.length;
                    int equalsCounter = 0;
                    while (String.valueOf(content.charAt(endClass)).equals(" ") || String.valueOf(content.charAt(endClass)).equals("=")) {
                        if (String.valueOf(content.charAt(endClass)).equals("=")) {
                            equalsCounter++;
                        }
                        endClass++;
                    }

                    ArrayList<String> stringList = new ArrayList<>();
                    if (equalsCounter == 1) {
                        if (String.valueOf(content.charAt(endClass)).equals("\"")) {
                            while (true) {
                                String s = String.valueOf(content.charAt(endClass + 1));
                                if (s.equals("\"")) break;
                                stringList.add(s);
                                endClass++;
                            }
                        }

                        // Convert array className --> String
                        StringBuilder bufferClassName = new StringBuilder();
                        for (String s : stringList) {
                            bufferClassName.append(s);
                        }
                        String className = bufferClassName.toString();
//--------------------------------------------------getValueFromClass---------------------------------------------------

                        ArrayList<String> val = new ArrayList<>();
                        boolean started = false;
                        if (className.equals(value)) {
                            int ending = index;
                            while (!String.valueOf(content.charAt(ending)).equals("<")) {
                                if (started) {
                                    val.add(String.valueOf(content.charAt(ending)));
                                }
                                if (String.valueOf(content.charAt(ending)).equals(">")) {
                                    started = true;
                                }
                                if (ending == content.length()-1) {
                                    break;
                                }
                                ending++;
                            }

                            // convert arrayValue --> String
                            StringBuilder bufferValue = new StringBuilder();
                            for (String s : val) {
                                bufferValue.append(s);
                            }
                            String answer = bufferValue.toString();
                            CharacterName.add(answer);
                        }
                    }
                }
            }
            index++;
        }
        return CharacterName;
    }
}