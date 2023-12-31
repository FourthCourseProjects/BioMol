package es.ulpgc.model.bio;

import es.ulpgc.Tuple;
import es.ulpgc.model.bio.acids.nucleic.NucleicAcid;
import es.ulpgc.model.bio.enzymes.DNAPolymerase;
import es.ulpgc.model.bio.enzymes.Helicase;
import es.ulpgc.model.bio.helixes.Helix;

import java.util.List;

import static es.ulpgc.model.bio.Chromatin.Chromosome.Chromatid;
import static es.ulpgc.model.bio.Chromatin.condense;

public class Cell {
    protected static final Helicase helicase = new Helicase();
    protected static final DNAPolymerase dnaPolymerase = new DNAPolymerase();
    public final Nucleus nucleus;

    public Cell(Nucleus nucleus) {
        this.nucleus = nucleus;
    }

    public Nucleus nucleus() {
        return nucleus;
    }


    public Tuple<Cell, Cell> selfReplicate() {
        return createCells(chromosomes(nucleus));
    }

    public static List<Chromatin.Chromosome> chromosomes(Nucleus nucleus) {
        return condense(dnaPolymerase.replicate(helicase.splitDNA(nucleus.chromatin.dna)));
    }

    private Tuple<Cell, Cell>  createCells(List<Chromatin.Chromosome> chromosomes) {
        return new Tuple<>(getCell(chromosomes.stream().map(Chromatin.Chromosome::firstChromatid).toList()),
                           getCell(chromosomes.stream().map(Chromatin.Chromosome::secondChromatid).toList()));
    }

    private Cell getCell(List<Chromatid> chromatids) {
        return new Cell(new Nucleus(new Chromatin(dnaFrom(chromatids), this.nucleus.chromatin.promoters)));
    }

    private Chromatin.DNA dnaFrom(List<Chromatid> chromatids) {
        return new Chromatin.DNA(helix(chromatids.stream().map(Chromatid::conductor).toList()),
                                 helix(chromatids.stream().map(Chromatid::delayed).toList()));
    }

    private Helix helix(List<List<NucleicAcid>> acids) {
        return new Helix(acids.stream().flatMap(List::stream).toList());
    }
}
