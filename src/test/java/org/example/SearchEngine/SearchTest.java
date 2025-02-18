package org.example.SearchEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    private Scanner scannerMock;  // Mock Scanner directly
    @Mock
    private File fileMock;

    @InjectMocks
    private Search search;  // The Search instance will have the mocked scanner injected

    @BeforeEach
    void setUp() {
        search = new Search();
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStreamCaptor));

    }

    @Test
    void testEnterDetailsErrorInReadingFile() throws IOException {

        when(scannerMock.hasNextLine()).thenReturn(true, true, false);  // First two calls return true, then false
        when(scannerMock.nextLine()).thenReturn("John Doe johndoe@example.com", "Jane Smith janesmith@example.com");

        // Call the enterDetails method with the mocked File (it will use our mocked Scanner)
        search.enterDetails(fileMock);

    }

    @Test
    void testEnterDetails() throws IOException {
        Path tempFile = Files.createTempFile("dummyFile", ".txt");

        // Write some dummy data into the file
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("John Doe johndoe@example.com\nJane Smith janesmith@example.com");
        }

        // Now use the File object for your test
        File file = tempFile.toFile();

        // Ensure the file exists and has content
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        // Call the enterDetails method with the actual file
        search.enterDetails(file);

        // You can now assert the contents of mp and wordMap
        assertEquals(2, search.mp.size());  // Ensure two entries are present
        assertTrue(search.mp.containsKey(0));  // First line
        assertTrue(search.mp.containsKey(1));  // Second line

        // Check wordMap to ensure it contains the expected words and indices
        assertTrue(search.wordMap.containsKey("john"));
        assertTrue(search.wordMap.containsKey("doe"));
        assertTrue(search.wordMap.containsKey("johndoe@example.com"));
        assertTrue(search.wordMap.containsKey("jane"));
        assertTrue(search.wordMap.containsKey("smith"));
        assertTrue(search.wordMap.containsKey("janesmith@example.com"));
    }


    @Test
    void testSearchQueryWithSearchAll() {
        // Setup the mock strategy to be SearchAll
        SearchInterface searchAllStrategy = new SearchAll();
        search.setSearchStrategy(searchAllStrategy);

        // Mock the user input and the method call
        when(scannerMock.nextLine()).thenReturn("john");  // Simulate user input "john"

        search.mp.put(0, "John Doe");
        search.mp.put(1, "Jane Smith");
        search.wordMap.put("john", new ArrayList<>(List.of(0)));

        search.searchQuery();  // Executes searchQuery with SearchAll strategy

        // Verify that nextLine() was called to get the search query
        verify(scannerMock, times(1)).nextLine();  // Check that nextLine() was called once
    }

    @Test
    void testSearchQueryWithSearchNone() {
        // Setup the mock strategy to be SearchNone
        SearchInterface searchNoneStrategy = new SearchNone();
        search.setSearchStrategy(searchNoneStrategy);

        // Mock the user input
        when(scannerMock.nextLine()).thenReturn("john");

        search.mp.put(0, "John Doe");
        search.mp.put(1, "Jane Smith");
        search.wordMap.put("john", new ArrayList<>(List.of(0)));

        search.searchQuery();  // Executes searchQuery with SearchNone strategy

        // Verify that nextLine() was called once
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    void testSearchQueryWithSearchAny() {
        // Setup the mock strategy to be SearchAny
        SearchInterface searchAnyStrategy = new SearchAny();
        search.setSearchStrategy(searchAnyStrategy);

        // Mock the user input
        when(scannerMock.nextLine()).thenReturn("john");

        search.mp.put(0, "John Doe");
        search.mp.put(1, "Jane Smith");
        search.wordMap.put("john", new ArrayList<>(List.of(0)));

        search.searchQuery();  // Executes searchQuery with SearchAny strategy

        // Verify that nextLine() was called once
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    void testSearchQuery_NoStrategySet() {
        when(scannerMock.nextLine()).thenReturn("John Doe");

        // Set the strategy to null
        search.setSearchStrategy(null);  // Explicitly set strategy to null to test the else block

        // Call the method under test
        search.searchQuery();

        // Verify that "No search strategy set." is printed to the console
        String expectedOutput = "Enter a name or email to search all suitable people.\n" +
                "No search strategy set.\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString(), "Expected message should be printed when strategy is null.");
    }

    @Test
    void testChooseOptionWithExitForAny() {
        // Mock the user selecting option 1 and then option 0 to exit
        when(scannerMock.nextInt()).thenReturn(1, 0);  // First option (Find a person), then exit

        // Mock the input for search query
        when(scannerMock.nextLine()).thenReturn("john", "ANY");  // Simulate selecting a search strategy

        // Simulate a scenario where the user selects the option to search and then exit
        search.chooseOption();
    }

    @Test
    void testChooseOptionWithExitForAll() {
        // Mock the user selecting option 1 and then option 0 to exit
        when(scannerMock.nextInt()).thenReturn(1, 0);  // First option (Find a person), then exit

        // Mock the input for search query
        when(scannerMock.nextLine()).thenReturn("john", "ALL");  // Simulate selecting a search strategy

        // Simulate a scenario where the user selects the option to search and then exit
        search.chooseOption();

    }

    @Test
    void testChooseOptionWithExitForNone() {
        // Mock the user selecting option 1 and then option 0 to exit
        when(scannerMock.nextInt()).thenReturn(1, 0);  // First option (Find a person), then exit

        // Mock the input for search query
        when(scannerMock.nextLine()).thenReturn("john", "NONE");  // Simulate selecting a search strategy

        // Simulate a scenario where the user selects the option to search and then exit
        search.chooseOption();
    }

    @Test
    void testChooseOptionWithExitForInvalid() {
        // Mock the user selecting option 1 and then option 0 to exit
        when(scannerMock.nextInt()).thenReturn(1, 0);  // First option (Find a person), then exit

        // Mock the input for search query
        when(scannerMock.nextLine()).thenReturn("john", "Invalid");  // Simulate selecting a search strategy

        // Simulate a scenario where the user selects the option to search and then exit
        search.chooseOption();
    }

    @Test
    void testChooseOptionWithExitForPrint() {
        // Mock the user selecting option 1 and then option 0 to exit
        when(scannerMock.nextInt()).thenReturn(2, 0);  // First option (Find a person), then exit

        // Mock the input for search query
        when(scannerMock.nextLine()).thenReturn("john", "Invalid");  // Simulate selecting a search strategy

        // Simulate a scenario where the user selects the option to search and then exit
        search.chooseOption();
    }

    @Test
    void testChooseOptionWithExitForPrinting() {
        when(scannerMock.nextInt()).thenReturn(2, 0);
        // Mock the user selecting option 1 and then option 0 to exit
        Map<Integer, String> samplePeople = new HashMap<>();
        samplePeople.put(0, "John Doe johndoe@example.com");
        samplePeople.put(1, "Jane Smith janesmith@example.com");
        search.mp = samplePeople;
        search.printPeople();

        // Pre-populating the search object with sample data for testing
        // Verify that the output matches the expected output
        String expectedOutput = "Found people:\nJohn Doe johndoe@example.com\nJane Smith janesmith@example.com\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void testChooseOptionWithExitForIncorrectOption() {
        when(scannerMock.nextInt()).thenReturn(3, 0);  // First option (Find a person), then exit

        search.chooseOption();
    }
}
