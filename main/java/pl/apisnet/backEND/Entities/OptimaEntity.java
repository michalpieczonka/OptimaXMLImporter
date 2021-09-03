package pl.apisnet.backEND.Entities;


import javax.persistence.*;

@Entity
@Table(name = "GlobalOptima")
public class OptimaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameterID")
    private int Id;

    @Column(name = "optimaVersion")
    private String optimaVersion;

    public OptimaEntity(){}

    public String getOptimaVersion(){
        return this.optimaVersion;
    }

}
