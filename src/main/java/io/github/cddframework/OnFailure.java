package io.github.cddframework;

public enum OnFailure {
    /** Continue the process despite the failure */
    CONTINUE,
    /** Log the failure but continue the process */
    LOG_ONLY,
    /** Fail the entire build on failure */
    FAIL_BUILD
}