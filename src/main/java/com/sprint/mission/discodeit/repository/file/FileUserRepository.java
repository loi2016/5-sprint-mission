package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final File file = new File("user.ser");
    private final Map<UUID, User> data;

    public FileUserRepository() {
        this.data = loadFromFile();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = this.data.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        this.data.remove(userId);
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
    private Map<UUID, User> loadFromFile() {
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) inputFile.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("로드 오류 발생 : " + e.getMessage());
            return new HashMap<>();
        }
    }
}
