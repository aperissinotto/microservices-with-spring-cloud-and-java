package br.com.perissinotto.controller;

import br.com.perissinotto.dto.Exchange;
import br.com.perissinotto.environment.InstanceInformationService;
import br.com.perissinotto.model.Book;
import br.com.perissinotto.proxy.ExchangeProxy;
import br.com.perissinotto.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private InstanceInformationService informationService;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ExchangeProxy proxy;

    @Operation(summary = "Find a specific book by your ID")
    @GetMapping(value = "/{id}/{currency}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(
            @PathVariable("id") Long id,
            @PathVariable("currency") String currency
    ){
        String port = informationService.retrieveServerPort();
        String host = informationService.retrieveInstanceInfo();

        var book = repository.findById(id).orElseThrow();

        logger.info("Calculating the converted price of the book from {} USD to {}.", book.getPrice(), currency);

        Exchange exchange = proxy.getExchange(book.getPrice(), "USD", currency);

        book.setEnvironment(
            "BOOK HOST: " + host + " PORT: " + port +
            " VERSION: kube-v1" +
            " EXCHANGE HOST: " + exchange.getEnvironment());
        book.setPrice(exchange.getConvertedValue());
        book.setCurrency(currency);
        return book;
    }
}