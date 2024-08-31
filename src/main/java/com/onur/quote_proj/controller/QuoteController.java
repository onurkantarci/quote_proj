package com.onur.quote_proj.controller;

import com.onur.quote_proj.model.Quote;
import com.onur.quote_proj.service.QuoteService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class QuoteController {

    @Autowired
    private QuoteService service;

    @GetMapping("/quotes")
    public ResponseEntity<Page<Quote>> getAllQuotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(service.getQuotes(page, size), HttpStatus.OK);
    }


//    @GetMapping("/quotes")
//    public ResponseEntity<List<Quote>> getAllQuotes() {
//        return new ResponseEntity<>(service.getAllQuotes(), HttpStatus.OK);
//    }

    @GetMapping("/quotes/random")
    public ResponseEntity<Quote> getRandomQuote() {
        Quote quote = service.getRandomQuote();
        if (quote != null) {
            return new ResponseEntity<>(quote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/quotes/search")
    public ResponseEntity<List<Quote>> searchQuotes(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String text) {
        List<Quote> results = service.searchQuotes(author, text);
        if (!results.isEmpty()) {
            return new ResponseEntity<>(results, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/quote/new")
    public ResponseEntity<?> addQuote(@RequestBody Quote quote) {
        try {
            Quote quote1 = service.addQuote(quote);
            return new ResponseEntity<>(quote1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/quote/{id}")
    public ResponseEntity<String> updateQuote(@PathVariable int id, @RequestBody Quote quote) {
        Quote updatedQuote = service.updateQuote(id, quote);
        if (updatedQuote != null) {
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/quote/{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable int id) {
        Quote quote = service.getQuote(id);
        if (quote != null) {
            service.deleteQuote(id);
            return new ResponseEntity<>("Quote deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Quote not found", HttpStatus.NOT_FOUND);
        }
    }
}
