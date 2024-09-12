package project3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * The Session class represents a login session, which includes a login record and a logout record.
 *
 * @author Leyan Yu
 */
public class Session implements Comparable<Session> {
    private Record login;
    private Record logout;

    /**
     * Constructs a new Session object.
     *
     * @param login  the login record.
     * @param logout the logout record.
     * @throws IllegalArgumentException    if the login record is null, or if the logout record is not a logout record.
     * @throws NoSuchElementException        if the usernames in the login and logout records do not match.
     */
    public Session(Record login, Record logout) {
        if (login == null) {
            throw new IllegalArgumentException("Login record cannot be null.");
        }
        if (logout != null && !logout.isLogout()) {
            throw new IllegalArgumentException("Invalid session records.");
        }
        if (logout != null && !login.getUsername().equals(logout.getUsername())) {
            throw new IllegalArgumentException("Usernames do not match.");
        }
        if (logout != null && login.getTerminal() != logout.getTerminal()) {
            throw new IllegalArgumentException("Terminal numbers do not match.");
        }
        if (logout != null && login.compareTo(logout) > 0) {
            throw new IllegalArgumentException("Login time is after logout time.");
        }
        this.login = login;
        this.logout = logout;
    }

    public int getTerminal() {
        return login.getTerminal();
    }

    public Date getLoginTime() {
        return login.getTime();
    }

    public Date getLogoutTime() {
        return logout == null ? null : logout.getTime();
    }

    public String getUsername() {
        return login.getUsername();
    }

    public long getDuration() {
        return logout == null ? -1 : logout.getTime().getTime() - login.getTime().getTime();
    }

    /**
     * Returns a string representation of this session.
     *
     * @return a string representation of this session.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String message = "";

        long diff = getDuration();
        if (diff == -1) {
            message += getUsername() + ", terminal " + getTerminal() + ", duration active session";
            message += "\n";
            message += " logged in: " + sdf.format(getLoginTime()) + "\n";
            message += " logged out: still logged in";
        } else {
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long duration = diff % (24 * 60 * 60 * 1000);
            long diffHours = duration / (60 * 60 * 1000);
            duration = duration % (60 * 60 * 1000);
            long diffMinutes = duration / (60 * 1000);
            duration = duration % (60 * 1000);
            long diffSeconds = duration / 1000;

            message += getUsername() + ", terminal " + getTerminal() + ", duration " + diffDays + " days, "
                    + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds";

            message += "\n";
            message += " logged in: " + sdf.format(getLoginTime()) + "\n";
            message += " logged out: " + sdf.format(getLogoutTime()) + "\n";
        }
        return message;
    }

    @Override
    public int compareTo(Session other) {
        // Compare sessions based on their login records
        return this.getLoginTime().compareTo(other.getLoginTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Session)) {
            return false;
        }
        Session other = (Session) obj;
        return this.login.equals(other.login) &&
                ((this.logout == null && other.logout == null) || this.logout.equals(other.logout));
    }
}
