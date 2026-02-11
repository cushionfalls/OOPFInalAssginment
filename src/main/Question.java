package main;

/**
 * Represents a quiz question with multiple-choice options.
 * Stores the question text, available options, and the correct answer.
 */
public class Question {

    /** Unique identifier for the question */
    private int id;

    /** Question text */
    private String questionText;

    /** Multiple-choice options */
    private String optionA, optionB, optionC, optionD;

    /** Correct option (e.g., A, B, C, or D) */
    private String correctOption;

    /**
     * Constructs a Question object with all details.
     *
     * @param id unique question ID
     * @param questionText the text of the question
     * @param optionA option A
     * @param optionB option B
     * @param optionC option C
     * @param optionD option D
     * @param correctOption the correct option
     */
    public Question(int id, String questionText, String optionA, String optionB,
                    String optionC, String optionD, String correctOption) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    /**
     * Returns the question text.
     *
     * @return question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Returns option A.
     *
     * @return option A
     */
    public String getOptionA() {
        return optionA;
    }

    /**
     * Returns option B.
     *
     * @return option B
     */
    public String getOptionB() {
        return optionB;
    }

    /**
     * Returns option C.
     *
     * @return option C
     */
    public String getOptionC() {
        return optionC;
    }

    /**
     * Returns option D.
     *
     * @return option D
     */
    public String getOptionD() {
        return optionD;
    }

    /**
     * Returns the correct option.
     *
     * @return correct option
     */
    public String getCorrectOption() {
        return correctOption;
    }
}
