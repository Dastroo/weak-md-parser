package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        StringBuilder result = new StringBuilder();
        boolean activeList = false;

        for (String line : lines) {

            String parsedLine = parseHeading(line);

            if (parsedLine == null) {
                parsedLine = parseListItem(line);
            }

            if (parsedLine == null) {
                parsedLine = parseParagraph(line);
            }

            if (parsedLine.startsWith("<li>") && !activeList) {
                activeList = true;
                result.append("<ul>");
            }

            if (!parsedLine.startsWith("<li>") && activeList) {
                activeList = false;
                result.append("</ul>");
            }

            parsedLine = parseEmphasisedTexts(parsedLine);

            result.append(parsedLine);
        }

        if (activeList) {
            result.append("</ul>");
        }

        return result.toString();
    }

    private String parseHeading(String markdown) {
        int count = 0;

        while (count < markdown.length() && markdown.charAt(count) == '#') {
            count++;
        }

        if (count == 0) {
            return null;
        }

        return String.format("<h%d>%s</h%d>", count, markdown.substring(count).trim(), count);
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
