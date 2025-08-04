package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final File file = new File("message.ser");
    private final Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data = loadFromFile();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.data.get(messageId);
        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public void delete(UUID messageId) {
        if (!this.data.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        this.data.remove(messageId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream outputFile = new ObjectOutputStream(new FileOutputStream(file))) {
            outputFile.writeObject(data);
        } catch (IOException e) {
            System.err.println("저장 오류 발생 : " + e.getMessage());
        }
    }

    @SuppressWarnings("NoProblem")
    private Map<UUID, Message> loadFromFile() {
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) inputFile.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("로드 오류 발생 : " + e.getMessage());
            return new HashMap<>();
        }
    }
}
