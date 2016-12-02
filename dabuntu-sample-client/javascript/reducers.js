import {VisibilityFilters, ADD_TODO, SET_VISIBILITY_FILTER, TOGGLE_TODO} from "./actions";
import {combineReducers} from "redux";


const initialState = {
    todos: [],
    filter: VisibilityFilters.SHOW_ALL,
}

function mainReducer(state = initialState, action) {
    switch (action.type) {
        case ADD_TODO:
            return Object.assign({}, state, {
                todos: [
                    ...state.todos,
                    {
                        text: action.text,
                        completed: false,
                    }
                ]
            })

        case SET_VISIBILITY_FILTER:
            return Object.assign({}, state, {
                filter: action.filter
            })

        case TOGGLE_TODO:
            return Object.assign({}, state, {
                todos: state.todos.map((el, index) => {
                    if (index === action.index) {
                        return Object.assign({}, el, {
                            completed: !el.completed
                        })
                    } else return el
                })
            })
        default:
            return state;
    }
}

function todos(state = [], action) {
    switch (action.type) {
        case ADD_TODO:
            return [
                ...state,
                {
                    text: action.text,
                    completed: false
                }
            ]
        case TOGGLE_TODO:
            return state.map((todo, index) => {
                if (index === action.index) {
                    return Object.assign({}, todo, {
                        completed: !todo.completed
                    })
                }
                return todo
            })
        default:
            return state
    }
}

function filter(state, action) {
    switch (action.type) {
        case SET_VISIBILITY_FILTER:
            return action.filter
        default:
            return state
    }
}

const reducer = combineReducers({
    todos,
    filter
})

export default reducer