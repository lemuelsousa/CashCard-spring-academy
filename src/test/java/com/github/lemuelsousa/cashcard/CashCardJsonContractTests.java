package com.github.lemuelsousa.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class CashCardJsonContractTests {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45, "lemuk4"),
                new CashCard(100L, 150.00, "lua10"),
                new CashCard(101L, 200.00, "1joana"));
    }

    @Test
    void cashCardSerializatinTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45, "lemuk4");
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("$.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("$.id").isEqualTo(99);

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("$.amount");
        assertThat(json.write(cashCard)).extractingJsonPathValue("$.amount").isEqualTo(123.45);

        assertThat(json.write(cashCard)).hasJsonPathStringValue("@.owner");
        assertThat(json.write(cashCard)).extractingJsonPathStringValue("@.owner").isEqualTo("lemuk4");
    }

    @Test
    void cashCardDeserializarionTest() throws IOException {
        String expected = """
                    {
                        "id": 99,
                        "amount": 123.45,
                        "owner": "lemuk4"
                    }
                """;

        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45, "lemuk4"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
        assertThat(json.parseObject(expected).owner()).isEqualTo("lemuk4");
    }

    @Test
    void cashCardListSerializarionTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cardCashDeserializationTest() throws IOException {
        String expected = """
                [
                    { "id": 99, "amount": 123.45, "owner": "lemuk4" },
                    { "id": 100, "amount": 150.00, "owner": "lua10"},
                    { "id": 101, "amount": 200.00, "owner": "1joana" }
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
