package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DinosaurService {

	@Autowired
	private DinosaurRepository dinosaurRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

	public List<Dinosaur> getDinosaursByCountryOfOrigin(String countryOfOrigin) {
		String sql = "SELECT * FROM dinosaurs WHERE country_of_origin = '" + countryOfOrigin + "'";
		return jdbcTemplate.query(sql, new DinosaurRowMapper());
	}

	static class DinosaurRowMapper implements org.springframework.jdbc.core.RowMapper<Dinosaur> {

		@Override
		public Dinosaur mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			Dinosaur dinosaur = new Dinosaur();
			dinosaur.setId(rs.getLong("id"));
			dinosaur.setName(rs.getString("name"));
			dinosaur.setSpecies(rs.getString("species"));
			dinosaur.setSex(rs.getString("sex"));
			dinosaur.setCountryOfOrigin(rs.getString("country_of_origin"));
			dinosaur.setNumberOfScales(rs.getInt("number_of_scales"));
			return dinosaur;
		}

	}

}