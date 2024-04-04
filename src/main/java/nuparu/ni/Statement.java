package nuparu.ni;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.annotation.Nullable;

import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;

public class Statement implements IChainable {

	public ArrayList<IChainable> chain = new ArrayList<IChainable>();

	public void addValue(Value value) {
		chain.add(value);
	}

	public void addValue(Object value) {
		if (value instanceof Double) {
			chain.add(new Value(EnumValueType.DOUBLE, value));
			return;
		}
		if (value instanceof Float) {
			chain.add(new Value(EnumValueType.FLOAT, value));
			return;
		}
		if (value instanceof Long) {
			chain.add(new Value(EnumValueType.LONG, value));
			return;
		}
		if (value instanceof Integer) {
			chain.add(new Value(EnumValueType.INT, value));
			return;
		}
		if (value instanceof Short) {
			chain.add(new Value(EnumValueType.SHORT, value));
			return;
		}
		if (value instanceof Byte) {
			chain.add(new Value(EnumValueType.BYTE, value));
			return;
		}
		if (value instanceof Boolean) {
			chain.add(new Value(EnumValueType.BOOL, value));
			return;
		}
		if (value instanceof String) {
			String s = (String) value;
			if (s.equals("true") || s.equals("false")) {
				chain.add(new Value(EnumValueType.BOOL, Boolean.parseBoolean(s)));
				return;
			}
			try {
				long l = Long.parseLong(s);
				if(Math.abs(l) <= Integer.MAX_VALUE) {
					int i = Integer.parseInt(s);
					chain.add(new Value(EnumValueType.INT, i));
					return;
				}
				chain.add(new Value(EnumValueType.LONG, l));
			} catch (Exception e) {
				try {
					int i = Integer.parseInt(s);
					chain.add(new Value(EnumValueType.INT, i));
				} catch (Exception e2) {
					chain.add(new Value(EnumValueType.STRING, s));
				}
			}
		}
	}

	public void addValue(Variable var) {
		// chain.add(new Value(EnumValueType.VAR, var));
	}

	public void addStatement(Statement statement) {
		chain.add(statement);
	}

	public void addOperator(Operator op) {
		chain.add(op);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public Value evaluate() throws EvaluationErrorException {

		// Gradually modified chain
		ArrayList<IChainable> modified = (ArrayList<IChainable>) chain.clone();

		//

		// Multiplication
		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("*")) {
				if (!it.hasPrevious() || !it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value valuePrev = prev.evaluate();
				Value valueNext = next.evaluate();

				if ((!valuePrev.isNumerical() && !valuePrev.isBoolean())
						|| (valuePrev.isNumerical() != valueNext.isNumerical())
						|| (valuePrev.isBoolean() != valueNext.isBoolean())) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = prev.evaluate().multiply(next.evaluate());

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();

			}
		}

		// 

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("/")) {
				if (!it.hasPrevious() || !it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				if (!prev.evaluate().isNumerical() || !next.evaluate().isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = prev.evaluate().divide(next.evaluate());

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();

			}
		}
		
		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("%")) {
				if (!it.hasPrevious() || !it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				if (!prev.evaluate().isNumerical() || !next.evaluate().isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = prev.evaluate().modulo(next.evaluate());

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();

			}
		}

		// 

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("-")) {
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}

				IChainable next = it.next();
				if (!next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				if (!next.evaluate().isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = next.evaluate().opposite();

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);

			}
		}

		// 

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("+")) {
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}

				IChainable next = it.next();
				if (!next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value result = next.evaluate();

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
			}
		}
		
		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("!")) {
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}

				IChainable next = it.next();
				if (!next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = next.evaluate().opposite();
				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
			}
		}

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("==")) {
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value result = new Value(prev.evaluate().equals(next.evaluate()));

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();
			}
		}

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("!=")) {
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value result = new Value(!prev.evaluate().equals(next.evaluate()));

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();
			}
		}

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals("<")) {
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value valuePrev = prev.evaluate();
				if (valuePrev == null || !valuePrev.isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value valueNext = next.evaluate();
				if (valueNext == null || !valueNext.isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = new Value(prev.evaluate().isLessThan(next.evaluate()));

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();
			}
		}

		for (ListIterator<IChainable> it = modified.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			IChainable ic = it.next();
			if (ic instanceof Operator && ((Operator) ic).value.equals(">")) {
				// previous got us 1? index back, we have to call previous() thrice and next()
				// twice!
				IChainable prev = it.previous();
				if (!it.hasPrevious()) {
					throw new EvaluationErrorException(this, ic);
				}
				prev = it.previous();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				IChainable next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!it.hasNext()) {
					throw new EvaluationErrorException(this, ic);
				}
				next = it.next();
				if (!prev.hasValue() || !next.hasValue()) {
					throw new EvaluationErrorException(this, ic);
				}

				Value valuePrev = prev.evaluate();
				if (valuePrev == null || !valuePrev.isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value valueNext = next.evaluate();
				if (valueNext == null || !valueNext.isNumerical()) {
					throw new EvaluationErrorException(this, ic);
				}
				Value result = new Value(prev.evaluate().isMoreThan(next.evaluate()));

				// We have to navigate back to the previous elements
				it.remove();
				it.previous();
				it.set(result);
				it.previous();
				it.remove();
			}
		}

		if (modified.size() >= 1  && modified.get(0) != null && modified.get(0).hasValue()) {
			Value value = modified.get(0).evaluate();
			if (value.isNumerical()) {
				for (int i = 1; i < modified.size(); i++) {
					if (!modified.get(i).hasValue()) {
						throw new EvaluationErrorException(this, modified.get(i));
					}
					Value other = modified.get(i).evaluate();
					if (!other.isNumerical()) {
						throw new EvaluationErrorException(this, other);
					}
					value = value.add(other);
				}
				return value;
			}
			if (value.isBoolean()) {
				for (int i = 1; i < modified.size(); i++) {
					if (!modified.get(i).hasValue()) {
						throw new EvaluationErrorException(this, modified.get(i));
					}
					Value other = modified.get(i).evaluate();
					if (!other.isBoolean()) {
						throw new EvaluationErrorException(this, other);
					}
					value = value.add(other);
				}
				return value;
			}
			if (value.isString()) {

				for (int i = 1; i < modified.size(); i++) {

					if (!modified.get(i).hasValue()) {
						throw new EvaluationErrorException(this, modified.get(i));
					}
					Value other = modified.get(i).evaluate();
					if (!other.isString()) {
						other = new Value(other.getRealValue().toString());
					}
					value = value.add(other);
				}
				return value;
			}
		}
		
		throw new EvaluationErrorException(this);
	}

	public String toString() {
		String s = "{ ";
		for (int i = 0; i < chain.size(); i++) {
			IChainable ic = chain.get(i);
			// if(ic instanceof Statement) continue;
			s += " " + ic.toString();
		}
		s += " }";
		return s;
	}

	public static String toString(ArrayList<IChainable> chain) {
		String s = "{";
		for (int i = 0; i < chain.size(); i++) {
			IChainable ic = chain.get(i);
			if (ic == null)
				continue;
			s += " " + ic.toString();
		}
		s += "}";
		return s;
	}

	@Override
	public boolean hasValue() {
		return true;
	}
}
