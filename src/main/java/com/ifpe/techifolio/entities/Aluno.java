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

    public boolean nullFieldAluno() {
        return this.getNome() == null || this.getEmail() == null || this.getSenha() == null || this.getFaculdade() == null;
    }
}
