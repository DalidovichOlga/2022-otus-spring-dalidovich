package ru.otus.spring.domain;

public class Question {
    String questionText;
    String variants;
    String trueAnswer;

    public Question(String questionText, String variants, String trueAnswer) {
        this.questionText = questionText;
        this.variants = variants;
        this.trueAnswer = trueAnswer;
    }

    public String getQuestionFullText() {
        if ("".equals(variants))
            return questionText;
        else
            return questionText + " Variants: " + variants;
    }

    public Boolean checkAnswer(String ans) {
        if ("".equals(variants)) {
            return trueAnswer.equalsIgnoreCase(ans);
        } else {
            if (trueAnswer.equalsIgnoreCase(ans)) return Boolean.TRUE;
            if ("12345".contains(ans)) {
                String[] arrayAnsw = variants.split(";");
                int i = 1;
                for (String str : arrayAnsw) {
                    if (str.equalsIgnoreCase(trueAnswer))
                        break;
                    i++;
                }
                return ans.equals(String.valueOf(i));

            }
            return false;

        }
    }
}
