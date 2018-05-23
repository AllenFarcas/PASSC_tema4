import java.util.ArrayList;
import java.util.List;

class Dots {
	protected List<Dot0> dot0;
	public List<Dots.Dot0> getDot0() {
			if (dot0 == null) {
			dot0 = new ArrayList<Dot0>();
		}
		return this.dot0;
	}
	public static class Dot0 {

		protected Integer x0;
		protected Integer y0;
		public Integer getX0() {return x0;}

		public Integer getY0() {return y0;}

		public void setX0(Integer value) {this.x0 = value;}

		public void setY0(Integer value) {this.y0 = value;}

	}
	protected List<Dot1> dot1;
	public List<Dots.Dot1> getDot1() {
			if (dot1 == null) {
			dot1 = new ArrayList<Dot1>();
		}
		return this.dot1;
	}
	public static class Dot1 {

		protected Integer x1;
		protected Integer y1;
		public Integer getX1() {return x1;}

		public Integer getY1() {return y1;}

		public void setX1(Integer value) {this.x1 = value;}

		public void setY1(Integer value) {this.y1 = value;}

	}
}

class ObjectFactory {
	public ObjectFactory() {}

	public Dots.Dot0 createDotsDot0() {
		return new Dots.Dot0();
	}


	public Dots.Dot1 createDotsDot1() {
		return new Dots.Dot1();
	}

	public Dots createDots() {
		return new Dots();
	}
}