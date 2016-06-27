package Task;

public class SpeciesDetector {

	Tree<String> t;
	Node<String> currentQuiz;

	public SpeciesDetector(Tree<String> t) {

		this.t = t;
		this.currentQuiz = t.getRoot();
	}

	public String getNextQuestion() {

		return currentQuiz.getElement();
	}

	public String submitAnswer(boolean answer) {

		if (answer == true) {

			currentQuiz = currentQuiz.getChildren().get(0);
			
			if (currentQuiz.getChildren().size() == 0) {
				return currentQuiz.getElement();
			} else {
				return null;
			}

		} else {

			currentQuiz = currentQuiz.getChildren().get(1);
			
			if (currentQuiz.getChildren().size() == 0) {
				return currentQuiz.getElement();
			} else {
				return null;
			}
			
		}
	}

	public static void main(String[] args) {

		Tree<String> t = new SimpleTree<String>();

		Node<String> question1 = new SimpleNode<String>("Does it have fur?");
		Node<String> question2 = new SimpleNode<String>("Does it have legs");
		Node<String> question3 = new SimpleNode<String>("Does it have ears");
		Node<String> question4 = new SimpleNode<String>("Does it laugh");

		Node<String> question2_ans1 = new SimpleNode<String>("Cat");
		Node<String> question2_ans2 = new SimpleNode<String>("Fish");
		Node<String> question3_ans1 = new SimpleNode<String>("BallCat");
		Node<String> question3_ans2 = new SimpleNode<String>("BallFish");
		Node<String> question4_ans1 = new SimpleNode<String>("smart");
		Node<String> question4_ans2 = new SimpleNode<String>("dumb");

		t.setRoot(question1);
		t.insert(question1, question2);
		t.insert(question1, question3);

		t.insert(question2, question2_ans1);
		t.insert(question2, question4);
		t.insert(question3, question3_ans1);
		t.insert(question3, question3_ans2);
		t.insert(question4, question4_ans1);
		t.insert(question4, question4_ans2);

		SpeciesDetector s = new SpeciesDetector(t);
		s.getNextQuestion(); // Returns "Does it have fur?"
		System.out.println(s.getNextQuestion());
		String species = s.submitAnswer(true); // Returns "Cat"
		System.out.println(species);
		String what = s.submitAnswer(false);
		System.out.println(what);
		String yo = s.submitAnswer(true);
		System.out.println(yo);

	}

}
