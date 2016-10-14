package org.dabuntu.web.core;

import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.container.ControllerAction;
import org.dabuntu.web.context.InstanceContainer;
import org.dabuntu.web.container.ResolvedArgument;
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
		ControllerAction action = actionContainer.getControllerAction();
		List<ResolvedArgument> rawBindings = actionContainer.getResolvedArguments();

		// get Controller
		Object controller = this.getController(action, instancePool);

		// find Arguments
		ResolvedArguments resolvedArguments = this.resolveArguments(action, rawBindings, instancePool);

		// execute Action
		ActionResult actionResult = this.executeAction(controller, action, resolvedArguments);

		return actionResult;
	}

	// ===================================================================================
	//                                                                  Resolve Controller
	//                                                                  ==================
	private Object getController(ControllerAction controllerAction, InstanceContainer instancesContainer) {
		Class controllerClass = controllerAction.getController();
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
	private ResolvedArguments resolveArguments(ControllerAction controllerAction,
											   List<ResolvedArgument> rawBindings,
											   InstanceContainer instanceContainer) {
		Object[] resolvedArgs = Arrays.stream(controllerAction.getAction().getParameters())
				.map(arg -> {
					for (ResolvedArgument rawBinding : rawBindings) {
						if (rawBinding.equals(arg)) {
							return arg.getType().cast(rawBinding.getValue());
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
	private ActionResult executeAction(Object controller, ControllerAction action, ResolvedArguments resolvedArguments) {

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
