package com.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Album;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AlbumRepository;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/album")

public class AlbumController {

	@Autowired
	AlbumRepository albumRepository;
	
	//取得所有相簿
	@GetMapping
	public Page<Album> getAllAlbums(Pageable pageable) {
		return albumRepository.findAll(pageable);
	}

	//新增相簿
	@PostMapping
	public Album createAlbum(@Valid @RequestBody Album album) {
		System.out.println(album.getName());
		return albumRepository.save(album);
	}
	
	//修改相簿
	@PutMapping("/{albumId}")
	public Album updateAlbum(@PathVariable Long albumId, @Valid @RequestBody Album albumRequest) {
		return albumRepository.findById(albumId).map(album -> {
			album.setName(albumRequest.getName());
			return albumRepository.save(album);
		}).orElseThrow(() -> new ResourceNotFoundException("AlbumId " + albumId + " not found", null, albumRequest));
	}
	//刪除相簿
	@DeleteMapping("/{albumId}")
	public ResponseEntity<?> deleteAlbum(@PathVariable Long albumId) {
		return albumRepository.findById(albumId).map(album -> {
			albumRepository.delete(album);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("AlbumId " + albumId + " not found", null, albumId));
	}
}
