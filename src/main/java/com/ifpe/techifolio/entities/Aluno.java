package com.ifpe.techifolio.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "alunos")
public class Aluno extends Pessoa{
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String faculdade;

    public Aluno(ObjectId id, String nome, String email, String senha, String faculdade) {
        super(nome, email, senha);
        this.id = id;
        this.faculdade = faculdade;
    }

    public String getNullFieldMessageAluno() {
        String returnText = "";
        if (this.getNome() == null) {
            returnText += "Nome não pode ser nulo. ";
        }
        if (this.getEmail() == null) {
            returnText += "Email não pode ser nulo. ";
        }
        if (this.getSenha() == null) {
            returnText += "Senha não pode ser nula. ";
        }
        if (this.getFaculdade() == null) {
            returnText += "Faculdade não pode ser nula. ";
        }
        if (returnText.isEmpty()) {
            return returnText;
        }
        return null;
    }
}
