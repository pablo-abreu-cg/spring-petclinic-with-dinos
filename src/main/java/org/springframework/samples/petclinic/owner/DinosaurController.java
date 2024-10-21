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

/**
 * DinosaurController is a REST controller that handles HTTP requests for managing
 * dinosaurs. It provides endpoints to perform CRUD operations on Dinosaur entities.
 *
 * Endpoints: - GET /dinosaurs: Retrieve a list of all dinosaurs. - GET /dinosaurs/{id}:
 * Retrieve a specific dinosaur by its ID. - POST /dinosaurs: Create a new dinosaur. - PUT
 * /dinosaurs/{id}: Update an existing dinosaur by its ID. - DELETE /dinosaurs/{id}:
 * Delete a dinosaur by its ID.
 *
 * This controller uses DinosaurService to interact with the underlying data store.
 *
 * Dependencies: - DinosaurService: Service layer for managing Dinosaur entities.
 *
 * Annotations: - @RestController: Indicates that this class is a REST controller.
 * - @RequestMapping("/dinosaurs"): Maps HTTP requests to /dinosaurs to this controller.
 * - @Autowired: Injects the DinosaurService dependency. - @GetMapping: Maps HTTP GET
 * requests to handler methods. - @PostMapping: Maps HTTP POST requests to handler
 * methods. - @PutMapping: Maps HTTP PUT requests to handler methods. - @DeleteMapping:
 * Maps HTTP DELETE requests to handler methods.
 *
 * Methods: - getAllDinosaurs(): Retrieves a list of all dinosaurs. - getDinosaurById(Long
 * id): Retrieves a specific dinosaur by its ID. - createDinosaur(Dinosaur dinosaur):
 * Creates a new dinosaur. - updateDinosaur(Long id, Dinosaur dinosaurDetails): Updates an
 * existing dinosaur by its ID. - deleteDinosaur(Long id): Deletes a dinosaur by its ID.
 */
@RestController
@RequestMapping("/dinosaurs")
public class DinosaurController {

	@Autowired
	private DinosaurService dinosaurService;

	@GetMapping
	public List<Dinosaur> getAllDinosaurs() {
		return dinosaurService.findAll();
	}

	/**
	 * Handles HTTP GET requests to retrieve a Dinosaur by its ID.
	 * @param id the ID of the Dinosaur to retrieve
	 * @return a ResponseEntity containing the Dinosaur if found, or a 404 Not Found
	 * status if not found
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Dinosaur> getDinosaurById(@PathVariable Long id) {
		Optional<Dinosaur> dinosaur = dinosaurService.findById(id);
		return dinosaur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Handles the HTTP POST request to create a new Dinosaur.
	 * @param dinosaur the Dinosaur object to be created, provided in the request body
	 * @return the created Dinosaur object
	 */
	@PostMapping
	public Dinosaur createDinosaur(@RequestBody Dinosaur dinosaur) {
		return dinosaurService.save(dinosaur);
	}

	/**
	 * Updates an existing Dinosaur with the provided details.
	 * @param id the ID of the Dinosaur to be updated
	 * @param dinosaurDetails the details to update the Dinosaur with
	 * @return a ResponseEntity containing the updated Dinosaur if found, or a
	 * ResponseEntity with a not found status if the Dinosaur does not exist
	 */
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

	/**
	 * Deletes a dinosaur by its ID.
	 * @param id the ID of the dinosaur to delete
	 * @return a ResponseEntity with status 204 (No Content) if the dinosaur was
	 * successfully deleted, or status 404 (Not Found) if the dinosaur with the specified
	 * ID does not exist
	 */
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

	@GetMapping("/country/{country}")
	public List<Dinosaur> getDinosaursByCountryOfOrigin(@PathVariable String country) {
		return dinosaurService.getDinosaursByCountryOfOrigin(country);
	}

}
