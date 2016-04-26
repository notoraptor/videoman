package videoman.control;

import videoman.form.Form;

public abstract class Controller<F extends Form> {
	abstract public <R extends F> void init(R element) throws Exception;
}