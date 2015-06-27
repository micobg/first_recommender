public class Main {

    public static void main(String[] args) {
        try {
            Recommender recommender = new Recommender();
            recommender.run();
        } catch(RecommenderException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
