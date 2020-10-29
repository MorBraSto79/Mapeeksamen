package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {

    public static void main(String[] args){
        Integer[] a = {4,7,2,9,4,10,8,7,4,6};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator. naturalOrder ());
        for ( int verdi : a) tre.leggInn(verdi);
        System. out .println(tre.antall()); // Utskrift: 10


    }
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        private Node(T verdi) {
            this.verdi = verdi;
        }


        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {       // Koden er kopiert fra kompendiet (5.2.3 a) og litt modifisert
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, forelder = null;       // gjorde byttet navn på q til forelder
        int cmp = 0;

        while (p != null){
            forelder = p;
            cmp = comp.compare(verdi, p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;
        }

        p = new Node<T>(verdi);

        if(forelder == null){
            rot = p;
        } else if (cmp < 0){
            forelder.venstre = p;
        } else {
            forelder.høyre = p;
        }

        antall++;
        return true;
        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public boolean fjern(T verdi) {     // importert fra kompendiet (5.2.8 d) og tilpasset denne oppgaven

        if (verdi == null){
            return false;
        }

        Node<T> p = rot;
        //forelder = null;

        while (p != null){
            int cmp = comp.compare(verdi,p.verdi);
            if (cmp < 0){
                //forelder = p;
                p = p.venstre;
            } else if (cmp > 0){
                //forelder = p;
                p = p.høyre;
            } else {
                break;
            }

            if (p == null){
                return false;
            }

            if (p.venstre == null || p.høyre == null){
                Node<T> barn = (p.venstre != null) ? p.venstre : p.høyre;
                if (p == rot){
                    rot = barn;
                    if (barn != null){
                        barn.forelder = null;
                    }
                } else if (p == p.forelder.venstre){
                    if(barn != null){
                        barn.forelder = p.forelder;
                    }
                    p.forelder.venstre = barn;
                } else {
                    if (barn != null){
                        barn.forelder = p.forelder;
                    }
                    p.forelder.høyre = barn;
                }
            } else {
                Node<T> r = p.høyre;
                while (r.venstre != null){
                    r = r.venstre;
                }
                p.verdi = r.verdi;
                if(r.forelder != p){
                    Node <T> q = r.forelder;
                    q.venstre = r.høyre;
                    if(q.venstre != null){
                        q.venstre.forelder = q;
                    }
                } else {
                    p.høyre = r.høyre;
                    if (p.høyre != null){
                        p.høyre.forelder = p;
                    }
                }
            }


        }
        antall--;
        return true;

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {

        if (tom()){
            return 0;
        }

        int fjernet = 0;
        boolean removed = true;
        while (removed != false){
            if(fjern(verdi)){
                fjernet++;
            } else {
                removed = false;
            }
        }
        return fjernet;

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int antall(T verdi) {

        if (inneholder(verdi) != true || tom() == true){
            return 0;
        }

        int stk = 0;
        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0){
                p = p.venstre;
            } else if (cmp > 0){
                p = p.høyre;
            } else {
                stk++;
                p = p.høyre;
            }
        }

        return stk;

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void nullstill() {
        rot = null;
        antall = 0;

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {

        if (p.venstre != null) {
            førstePostorden(p.venstre);
            return p.venstre;
        }
        if (p.høyre != null) {
            førstePostorden(p.høyre);
            return p.høyre;
        }
        return p;
        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {

        // Rot har ingen neste i postOrden

        Node <T> n = førstePostorden(p);
        if (n == p)
            return null;

        // If given node is right child of its  Hvis noden er foreldrens høyre barn, eller forlder ikke har høyre barn
        // så er forelder neste i postorden

        Node <T> m = n.forelder;
        if (m.høyre == null || m.høyre == n) {
            return m;
        }

        // I alle andre tilfeller leter vi etter venstrebarn i forelders høyre subtre

        Node <T> current = m.høyre;
        while (current.venstre != null)
            current = current.venstre;

        return current;

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postorden(Oppgave <? super T> oppgave){




        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


} // ObligSBinTre
