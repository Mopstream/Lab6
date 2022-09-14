package com.mopstream.server.utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.stream.Collectors;

import com.mopstream.common.data.Discipline;
import com.mopstream.common.data.LabWork;
import com.mopstream.common.exceptions.CollectionIsEmptyException;
import com.mopstream.common.exceptions.QueueIsFullException;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private PriorityQueue<LabWork> labsCollection = new PriorityQueue<>();
    private LocalDate lastInitTime;
    private LocalDate lastSaveTime;
    private static long lastId = 1;
    private static final Stack<Long> freeId = new Stack<>();
    private final FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        loadCollection();
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDate getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDate getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return The collecton itself.
     */
    public PriorityQueue<LabWork> getCollection() {
        return labsCollection;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return labsCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return labsCollection.size();
    }

    /**
     * @param id ID of the lab.
     * @return A lab by its ID or null if lab isn't found.
     */
    public LabWork getById(Long id) {
        return labsCollection.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * @param labWorkToFind A lab which's value will be found.
     * @return A lab by its value or null if lab isn't found.
     */
    public LabWork getByValue(LabWork labWorkToFind) {
        return labsCollection.stream().filter(x -> x.equals(labWorkToFind)).findFirst().orElse(null);
    }

    /**
     * Adds a new lab to collection.
     *
     * @param labWork A lab to add.
     */
    public void addToCollection(LabWork labWork) {
        labsCollection.add(labWork);
    }

    /**
     * @param disciplineToFilter Discipline to filter by.
     * @return Information about valid labs or empty string, if there's no such labs.
     */
    public String disciplineFilteredInfo(Discipline disciplineToFilter) {
        return labsCollection.stream().filter(x -> x.getDiscipline().equals(disciplineToFilter))
                .map(LabWork::toString)
                .collect(Collectors.joining("\n\n"));

                //.reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * Removes a lab from collection.\
     *
     * @param labWork that needs to be deleted
     */
    public void removeFromCollection(LabWork labWork) {
        freeId.push(labWork.getId());
        labsCollection.remove(labWork);
    }

    /**
     * Prints the elements of the collection in ascending order.
     */
    public String printAskending() {
        return labsCollection.stream().sorted().map(LabWork::toString).collect(Collectors.joining("\n\n"));
    }

    /**
     * @param minimalPoint Minimal Point to filter by
     * @return info of the elements whose minimum Point field value is less than the specified one
     * @throws CollectionIsEmptyException if the collection is empty
     */
    public String filterByMinimalPoint(long minimalPoint) throws CollectionIsEmptyException {
        if (labsCollection.isEmpty()) throw new CollectionIsEmptyException();
        return labsCollection.stream().filter(x -> x.getMinimalPoint()< minimalPoint).reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * Removes all items smaller than the specified one from the collection
     *
     * @param labWorkToCompare lab to filter by
     */
    public void removeLower(LabWork labWorkToCompare) {
        labsCollection.removeIf(labWork -> labWork.compareTo(labWorkToCompare) < 0);
    }

    /**
     * Removes head of the collection
     */
    public void removeFirst() {
        freeId.push(labsCollection.peek().getId());
        labsCollection.poll();
    }

    /**
     *
     */
    public LabWork getHead() {
        return labsCollection.peek();
    }

    /**
     * Clears the collection
     */
    public void clearCollection() {
        labsCollection.clear();
    }

    /**
     * Generates next ID.
     *
     * @return Next ID.
     * @throws QueueIsFullException if collection is full
     */
    public Long generateNextId() throws QueueIsFullException {
        if (freeId.isEmpty()) {
            while (getById(lastId) != null) {
                lastId++;
            }
            if (lastId < Long.MAX_VALUE) {
                return lastId++;
            } else {
                throw new QueueIsFullException();
            }
        } else {
            return freeId.pop();
        }
    }

    /**
     * Saves the colletion to file
     */
    public void saveCollection() {
        fileManager.writeCollection(labsCollection);
        lastSaveTime = LocalDate.now();
    }

    /**
     * Loads the collection from file
     */
    private void loadCollection() {
        labsCollection = fileManager.readCollection();
        lastInitTime = LocalDate.now();
    }

    public String showCollection() {
        if (labsCollection.isEmpty()) return "Коллекция пуста!";
        return labsCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

}
