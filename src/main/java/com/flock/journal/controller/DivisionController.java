package com.flock.journal.controller;

import com.flock.journal.model.Division;
import com.flock.journal.service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/divisions")
public class DivisionController {

  private final DivisionService divisionService;

  @Autowired
  public DivisionController(DivisionService divisionService) {
    this.divisionService = divisionService;
  }

  @GetMapping
  public ResponseEntity<List<Division>> getAllDivisions() {
    List<Division> divisions = divisionService.getAllDivisions();
    return new ResponseEntity<>(divisions, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Division> getDivisionById(@PathVariable("id") Long id) {
    Optional<Division> division = divisionService.getDivisionById(id);
    return division.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Division> createDivision(@RequestBody Division division) {
    Division savedDivision = divisionService.saveDivision(division);
    return new ResponseEntity<>(savedDivision, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDivision(@PathVariable("id") Long id) {
    divisionService.deleteDivision(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}