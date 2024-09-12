package project3;

import java.util.NoSuchElementException;

/**
 * The RecordList class extends SortedLinkedList<Record> and provides methods to work with login sessions.
 *
 * @author Leyan Yu
 */
public class RecordList extends SortedLinkedList<Record> {
    // Default constructor to create an empty RecordList
    public RecordList() {
        // Call the constructor of the parent class (SortedLinkedList) to initialize the list
        super();
    }

    /**
     * Returns the first login session for the specified user.
     *
     * @param user the name of the user.
     * @return the first login session for the user.
     * @throws IllegalArgumentException if the user is null or empty.
     * @throws NoSuchElementException if no first session is found for the user.
     */
    public Session getFirstSession(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid user!");
        }

        Record firstLoginRecord = null;

        int i = 0;
        // find the first login record
        for (; i < this.size(); i++) {
            if (this.get(i).getUsername().equals(user) && this.get(i).isLogin()) {
                firstLoginRecord = this.get(i);
                break;
            }
        }
        // if have not found the first login record
        if (firstLoginRecord == null) {
            throw new NoSuchElementException("No first session found for user " + user);
        }
        // continue to find the corresponding logout record with the same user, terminal.
        for (; i < this.size(); i++) {
            if (this.get(i).getUsername().equals(user) &&
                    this.get(i).getTerminal() == firstLoginRecord.getTerminal() &&
                    this.get(i).isLogout())
                return new Session(firstLoginRecord, this.get(i));
        }
        //no corresponding logout record
        return new Session(firstLoginRecord, null);


    }

    /**
     * Returns the last login session for the specified user.
     *
     * @param user the name of the user.
     * @return the last login session for the user.
     * @throws IllegalArgumentException if the user is null or empty.
     * @throws NoSuchElementException if no last session is found for the user.
     */
    public Session getLastSession(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid user!");
        }

        Record lastLoginRecord = null;
        int i = this.size() - 1;
        // Iterate through the records in reverse order to find the last login record
        for (; i >= 0; i--) {
            if (this.get(i).getUsername().equals(user) && this.get(i).isLogin()) {
                lastLoginRecord = this.get(i);
                break;
            }
        }

        if (lastLoginRecord == null) {
            throw new NoSuchElementException("No last session found for user " + user);
        }

        // Continue to find the corresponding logout record with the same user and terminal
        for (; i < this.size(); i++) {
            if (this.get(i).getUsername().equals(user) &&
                    this.get(i).getTerminal() == lastLoginRecord.getTerminal() &&
                    this.get(i).isLogout()) {
                return new Session(lastLoginRecord, this.get(i));
            }
        }

        // No corresponding logout record, which means the session is still active
        return new Session(lastLoginRecord, null);
    }

    /**
     * Returns the total amount of time in milliseconds that the user was logged in.
     *
     * @param user the name of the user.
     * @return the total login time in milliseconds for the user.
     * @throws IllegalArgumentException if the user is null or empty.
     * @throws NoSuchElementException if the user is not found in the records.
     */
    public long getTotalTime(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid user!");
        }

        long totalTime = 0;

        for (int i = 0; i < this.size(); i++) { // find login record
            if (this.get(i).getUsername().equals(user) && this.get(i).isLogin()) {
                for (int j = i+1; j < this.size(); j++) { // find the corresponding logout record
                    if (this.get(j).getUsername().equals(user) &&
                            this.get(j).getTerminal() == this.get(i).getTerminal() &&
                            this.get(j).isLogout()) {
                        totalTime += this.get(j).getTime().getTime() - this.get(i).getTime().getTime();
                        break;
                    }
                }
            }
        }

        if (totalTime == 0) {
            throw new NoSuchElementException("User " + user + " not found in the records.");
        }

        return totalTime;
    }

    /**
     * Returns a list of all login sessions for the specified user, ordered by login time.
     *
     * @param user the name of the user.
     * @return a list of all login sessions for the user.
     * @throws IllegalArgumentException if the user is null or empty.
     * @throws NoSuchElementException if no sessions are found for the user.
     */
    public SortedLinkedList<Session> getAllSessions(String user) {
        if (user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid user!");
        }
        SortedLinkedList<Session> sessions = new SortedLinkedList<>();

        for (int i = 0; i < this.size(); i++) { // find login record
            if (this.get(i).getUsername().equals(user) && this.get(i).isLogin()) {
                int j = i+1;
                for (; j < this.size(); j++) { // find the corresponding logout record
                    if (this.get(j).getUsername().equals(user) &&
                            this.get(j).getTerminal() == this.get(i).getTerminal() &&
                            this.get(j).isLogout()) {
                        sessions.add(new Session(this.get(i), this.get(j)) );
                        break;
                    }
                }
                if (j == this.size()) {
                    sessions.add(new Session(this.get(i), null) );
                }
            }
        }


        if (sessions.size() == 0) {
            throw new NoSuchElementException("No sessions found for user " + user);
        }

        return sessions;
    }
}
