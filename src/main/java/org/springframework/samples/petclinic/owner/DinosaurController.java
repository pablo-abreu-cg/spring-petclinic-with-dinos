package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dinosaurs")
public class DinosaurController {

	@Autowired
	private DinosaurService dinosaurService;

	@GetMapping
	public List<Dinosaur> getAllDinosaurs() {
		return dinosaurService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Dinosaur> getDinosaurById(@PathVariable Long id) {
		Optional<Dinosaur> dinosaur = dinosaurService.findById(id);
		return dinosaur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public Dinosaur createDinosaur(@RequestBody Dinosaur dinosaur) {
		return dinosaurService.save(dinosaur);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Dinosaur> updateDinosaur(@PathVariable Long id, @RequestBody Dinosaur dinosaurDetails) {
		Optional<Dinosaur> optionalDinosaur = dinosaurService.findById(id);
		if (optionalDinosaur.isPresent()) {
			Dinosaur dinosaur = optionalDinosaur.get();
			dinosaur.setName(dinosaurDetails.getName());
			dinosaur.setSpecies(dinosaurDetails.getSpecies());
			dinosaur.setSex(dinosaurDetails.getSex());
			dinosaur.setCountryOfOrigin(dinosaurDetails.getCountryOfOrigin());
			dinosaur.setNumberOfScales(dinosaurDetails.getNumberOfScales());
			return ResponseEntity.ok(dinosaurService.save(dinosaur));
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDinosaur(@PathVariable Long id) {
		if (dinosaurService.findById(id).isPresent()) {
			dinosaurService.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

}
