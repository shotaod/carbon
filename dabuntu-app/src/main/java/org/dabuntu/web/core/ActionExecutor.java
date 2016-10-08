package org.dabuntu.web.core;

import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.container.InstanceContainer;
import org.dabuntu.web.container.UriBindAction;
import org.dabuntu.web.container.raw.RawUrlVariableBinding;
import org.dabuntu.web.exception.ActionInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ubuntu 2016/10/07.
 */
public class ActionExecutor {
	private class ResolvedArguments {
		private Object[] args;

		public ResolvedArguments(Object[] args) {
			this.args = args;
		}

		public boolean emptyArg() {
			return this.args == null || this.args.length == 0;
		}
	}

	public ActionResult execute(ActionContainer actionContainer, InstanceContainer instancePool) {
		UriBindAction action = actionContainer.getAction();
		List<RawUrlVariableBinding> rawBindings = actionContainer.getRawBindings();

		// get Controller
		Object controller = this.getController(action, instancePool);

		// resolve Arguments
		ResolvedArguments resolvedArguments = this.resolveArguments(action, rawBindings, instancePool);

		// execute Action
		ActionResult actionResult = this.executeAction(controller, action, resolvedArguments);

		return actionResult;
	}

	// ===================================================================================
	//                                                                  Resolve Controller
	//                                                                  ==================
	private Object getController(UriBindAction action, InstanceContainer instancesContainer) {
		Class controllerClass = action.getControllerClass();
		Object controllerInstance = findInstance(controllerClass, instancesContainer.getInstances());

		return controllerInstance;
	}

	private Object findInstance(Class target, Map<Class, Object> instances) {
		Object object = instances.get(target);
		try {
			return target.cast(object);
		} catch (ClassCastException e) {
			throw actionInvokeException(target, e);
		}
	}

	// ===================================================================================
	//                                                                   Resolve Arguments
	//                                                                   =================
	private ResolvedArguments resolveArguments(UriBindAction bindAction,
											   List<RawUrlVariableBinding> rawBindings,
											   InstanceContainer instanceContainer) {
		Object[] resolvedArgs = Arrays.stream(bindAction.getAction().getParameters())
				.map(arg -> {
					for (RawUrlVariableBinding rawBinding : rawBindings) {
						if (rawBinding.equals(arg)) {
							return arg.getType().cast(rawBinding.getUrlPartValue());
						}
					}

					return arg;
				})
				.toArray();

		return new ResolvedArguments(resolvedArgs);
	}

	// ===================================================================================
	//                                                                      Execute Method
	//                                                                      ==============
	private ActionResult executeAction(Object controller, UriBindAction action, ResolvedArguments resolvedArguments) {

		Object result = null;
		try {
			Method actionMethod = action.getAction();
			if (resolvedArguments.emptyArg()) {
				result = actionMethod.invoke(controller);
			} else {
				result = actionMethod.invoke(controller, resolvedArguments.args);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return new ActionResult(result);

	}

	private ActionInvokeException actionInvokeException (Class target, ClassCastException e) {
		String message = String.format("failed to Invoke Controller [%s]", target.getName());
		return new ActionInvokeException(message, e);
	}

}
