package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@Component
public class DataLoader implements CommandLineRunner {

    private final ContestRepository contestRepository;

    @Autowired
    public DataLoader(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (contestRepository.count() == 0) {
            Contest contest = new Contest("Shodh-a-Code Mega Contest", "50 diverse coding problems to practice Java.");

            List<Problem> problems = new ArrayList<>();

            problems.add(new Problem("Sum of Two Numbers", "Add two integers and print the result."));
            problems.add(new Problem("Reverse String", "Reverse a given string input."));
            problems.add(new Problem("Factorial", "Compute factorial of a given integer N."));
            problems.add(new Problem("Palindrome Check", "Check whether a given string is a palindrome."));
            problems.add(new Problem("Fibonacci Sequence", "Print the first N terms of the Fibonacci sequence."));
            problems.add(new Problem("Prime Check", "Check whether a given number is prime or not."));
            problems.add(new Problem("Count Vowels", "Count the number of vowels in a string."));
            problems.add(new Problem("Armstrong Number", "Check if a number is an Armstrong number."));
            problems.add(new Problem("GCD of Two Numbers", "Find the greatest common divisor of two integers."));
            problems.add(new Problem("LCM of Two Numbers", "Find the least common multiple of two integers."));
            problems.add(new Problem("Sum of Digits", "Find the sum of all digits in an integer."));
            problems.add(new Problem("Largest of Three Numbers", "Find the largest among three numbers."));
            problems.add(new Problem("Even or Odd", "Check whether a number is even or odd."));
            problems.add(new Problem("Leap Year Check", "Determine whether a given year is a leap year."));
            problems.add(new Problem("Simple Interest", "Calculate simple interest given principal, rate, and time."));
            problems.add(new Problem("Temperature Converter", "Convert Celsius to Fahrenheit."));
            problems.add(new Problem("String Length", "Find the length of a given string."));
            problems.add(new Problem("Character Frequency", "Count occurrences of each character in a string."));
            problems.add(new Problem("Power of Number", "Calculate A raised to the power B."));
            problems.add(new Problem("Perfect Number", "Check if a number is perfect or not."));
            problems.add(new Problem("Sum of Array Elements", "Find the sum of all elements in an integer array."));
            problems.add(new Problem("Average of Numbers", "Compute average of given N numbers."));
            problems.add(new Problem("Second Largest Element", "Find the second largest number in an array."));
            problems.add(new Problem("Linear Search", "Search for an element in an array using linear search."));
            problems.add(new Problem("Binary Search", "Implement binary search on a sorted array."));
            problems.add(new Problem("Bubble Sort", "Sort an array using bubble sort algorithm."));
            problems.add(new Problem("Selection Sort", "Sort an array using selection sort algorithm."));
            problems.add(new Problem("Insertion Sort", "Sort an array using insertion sort algorithm."));
            problems.add(new Problem("Matrix Addition", "Add two matrices of same dimensions."));
            problems.add(new Problem("Matrix Multiplication", "Multiply two matrices."));
            problems.add(new Problem("Transpose Matrix", "Find the transpose of a matrix."));
            problems.add(new Problem("Sum of Natural Numbers", "Compute sum of first N natural numbers."));
            problems.add(new Problem("Palindrome Number", "Check if a number reads same backward as forward."));
            problems.add(new Problem("Count Words in String", "Count total words in a given sentence."));
            problems.add(new Problem("Remove Whitespaces", "Remove all whitespaces from a string."));
            problems.add(new Problem("Swap Two Numbers", "Swap values of two variables without using a third."));
            problems.add(new Problem("ASCII Value Finder", "Find ASCII value of a given character."));
            problems.add(new Problem("Multiplication Table", "Print multiplication table for a given number."));
            problems.add(new Problem("Sum of Even Numbers", "Find sum of all even numbers up to N."));
            problems.add(new Problem("Sum of Odd Numbers", "Find sum of all odd numbers up to N."));
            problems.add(new Problem("Decimal to Binary", "Convert decimal number to binary."));
            problems.add(new Problem("Binary to Decimal", "Convert binary number to decimal."));
            problems.add(new Problem("Count Digits", "Count total number of digits in an integer."));
            problems.add(new Problem("Strong Number", "Check whether a number is a Strong number."));
            problems.add(new Problem("Power of 2 Check", "Check if a number is a power of 2."));
            problems.add(new Problem("Factor Count", "Count number of factors of a number."));
            problems.add(new Problem("Reverse Number", "Reverse digits of an integer."));
            problems.add(new Problem("Sum of Primes", "Find the sum of all prime numbers up to N."));
            problems.add(new Problem("Array Reversal", "Reverse elements of an array."));
            problems.add(new Problem("Matrix Diagonal Sum", "Find the sum of both diagonals of a square matrix."));
            problems.add(new Problem("String Comparison", "Compare two strings lexicographically."));

            for (Problem p : problems) {
                contest.addProblem(p);
            }

            contestRepository.save(contest);

            System.out.println("✅ Loaded 50 coding problems successfully!");
        }
    }
}
