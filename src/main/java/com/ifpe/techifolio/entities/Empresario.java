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
@Document(collection = "empresarios")
public class Empresario extends Pessoa{
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String empresa;

    public boolean nullFieldEmpresario() {
        return this.getNome() == null || this.getEmail() == null || this.getSenha() == null || this.getEmpresa() == null;
    }
}
