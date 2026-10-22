package com.velasco.song;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping(path={"/mangulabnan/songs", "/velasco/songs", "/reyes/songs", "/songs"})
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @PostMapping
    public ResponseEntity createSong(@RequestBody Song song, HttpServletRequest request) throws URISyntaxException {
        Song savedSong = songRepository.save(song);
        String basePath = request != null && request.getRequestURI() != null ? request.getRequestURI() : "/mangulabnan/songs";
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        return ResponseEntity.created(new URI(basePath + "/" + savedSong.getId())).body(savedSong);
    }

    @GetMapping
    public @ResponseBody Iterable<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getSongById(@PathVariable Integer id) {
        Optional<Song> song = songRepository.findById(id);

        if (song.isPresent()) {
            return ResponseEntity.ok(song.get());
        }

        return ResponseEntity.status(404).body("Song with id " + id + " not found");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateSong(@PathVariable Integer id, @RequestBody Song updated) {
        Optional<Song> existingOpt = songRepository.findById(id);

        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Song with id " + id + " not found.");
        }

        Song existing = existingOpt.get();

        // Follow your Song entity fields exactly
        existing.setTitle(updated.getTitle());
        existing.setArtist(updated.getArtist());
        existing.setGenre(updated.getGenre());
        existing.setAlbum(updated.getAlbum());
        existing.setUrl(updated.getUrl());

        Song saved = songRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteSong(@PathVariable Integer id) {
        Optional<Song> song = songRepository.findById(id);

        if (song.isPresent()) {
            songRepository.deleteById(id);
            return ResponseEntity.ok("Song with id " + id + " deleted.");
        }

        return ResponseEntity.status(404).body("Song with id " + id + " not found.");
    }

}