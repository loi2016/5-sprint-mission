package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final File file = new File("channel.ser");
    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public void delete(UUID channelId) {
        if (!this.data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        this.data.remove(channelId);
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
    private Map<UUID, Channel> loadFromFile() {
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) inputFile.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("로드 오류 발생 : " + e.getMessage());
            return new HashMap<>();
        }
    }
}
