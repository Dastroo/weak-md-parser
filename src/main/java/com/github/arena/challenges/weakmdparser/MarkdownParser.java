package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        String result = "";
        boolean activeList = false;

        for (int i = 0; i < lines.length; i++) {

            String parsedLine = parseHeading(lines[i]);

            if (parsedLine == null) {
                parsedLine = parseListItem(lines[i]);
            }

            if (parsedLine == null) {
                parsedLine = parseParagraph(lines[i]);
            }

            parsedLine = parseEmphasisedTexts(parsedLine);

            if (parsedLine.matches("(<li>).*") && !parsedLine.matches("(<h).*") && !parsedLine.matches("(<p>).*") && !activeList) {
                activeList = true;
                result = result + "<ul>";
                result = result + parsedLine;
            } else if (!parsedLine.matches("(<li>).*") && activeList) {
                activeList = false;
                result = result + "</ul>";
                result = result + parsedLine;
            } else {
                result = result + parsedLine;
            }
        }

        if (activeList) {
            result = result + "</ul>";
        }

        return result;
    }

    private String parseHeading(String markdown) {
        int count = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            count++;
        }

        if (count == 0) {
            return null;
        }

        return "<h" + Integer.toString(count) + ">" + markdown.substring(count + 1) + "</h" + Integer.toString(count) + ">";
    }

    private String parseListItem(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            return "<li>" + skipAsterisk + "</li>";
        }

        return null;
    }

    private String parseParagraph(String markdown) {
        return "<p>" + markdown + "</p>";
    }

    private String parseEmphasisedTexts(String markdown) {

        String lookingFor = "__(.+)__";
        String update = "<strong>$1</strong>";
        String workingOn = markdown.replaceAll(lookingFor, update);

        lookingFor = "_(.+)_";
        update = "<em>$1</em>";
        return workingOn.replaceAll(lookingFor, update);
    }
}
