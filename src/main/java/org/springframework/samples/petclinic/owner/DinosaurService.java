package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DinosaurService {

	@Autowired
	private DinosaurRepository dinosaurRepository;

	public List<Dinosaur> findAll() {
		return dinosaurRepository.findAll();
	}

	public Optional<Dinosaur> findById(Long id) {
		return dinosaurRepository.findById(id);
	}

	public Dinosaur save(Dinosaur dinosaur) {
		return dinosaurRepository.save(dinosaur);
	}

	public void deleteById(Long id) {
		dinosaurRepository.deleteById(id);
	}

}