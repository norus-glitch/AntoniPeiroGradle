package com.antonipeiro.tema4gradle;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        OpenAiChatModel modelTrabajador = OpenAiChatModel.builder()
                .baseUrl("http://localhost:11434/v1")
                .modelName("llama3.1:8b")
                .build();

        OpenAiChatModel modelInspector = OpenAiChatModel.builder()
                .baseUrl("http://localhost:11434/v1")
                .modelName("llama3.1:8b")
                .build();

        List<ChatMessage> historyTrabajador = new ArrayList<>();
        historyTrabajador.add(new SystemMessage(
                "Eres un trabajador torpe y descuidado con la higiene. " +
                        "Responde SIEMPRE con texto plano, máximo 5-6 frases, nunca vacío." +
                            "Escribe acciones entre *"
        ));


        List<ChatMessage> historyInspector = new ArrayList<>();
        historyInspector.add(new SystemMessage(
                "Eres un Inspector de la EU-OSHA cínico y perfeccionista. " +
                        "Responde SIEMPRE con texto plano, máximo 2-3 frases." +
                            "Escribe acciones entre *"
        ));


        historyTrabajador.add(new UserMessage(
                    "Estás esperando en la encimera de una panadería aburrido, y entra un inspector por la puerta (yo)."
        ));

        for (int i = 0; i < 3; i++) {

            // Trabajador habla
            AiMessage respuestaTrabajador = modelTrabajador.chat(historyTrabajador).aiMessage();
            historyTrabajador.add(respuestaTrabajador);

            String textoTrabajador = respuestaTrabajador.text();
            if (textoTrabajador == null || textoTrabajador.isBlank()) {
                textoTrabajador = "[El trabajador no respondió]";
            }

            System.out.println("Trabajador: " + textoTrabajador);

            // Pasar al Inspector
            historyInspector.add(new UserMessage(textoTrabajador));

            // Inspector responde
            AiMessage respuestaInspector = modelInspector.chat(historyInspector).aiMessage();
            historyInspector.add(respuestaInspector);

            String textoInspector = respuestaInspector.text();
            if (textoInspector == null || textoInspector.isBlank()) {
                textoInspector = "[El inspector no respondió]";
            }

            System.out.println("Inspector: " + textoInspector);

            // Pasar de vuelta al Trabajador
            historyTrabajador.add(new UserMessage(textoInspector));
        }
    }
}