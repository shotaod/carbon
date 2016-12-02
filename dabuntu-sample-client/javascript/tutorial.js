import * as React from "react";
import * as ReactDOM from "react-dom";

function Square(props) {
    return (
        <button className="square" onClick={() => props.onClick()}>
            {props.value}
        </button>
    );
}

class Board extends React.Component {
    renderBoard(size) {
        return Array(size).fill().map((_, i) => this.renderRow(size, i))
    }
    renderRow(size, row) {
        return (
            <div key={'row_' + row} className="board-row">
                {Array(size).fill().map((_, i)=> this.renderSquare(size * row + i))}
            </div>
        );
    }
    renderSquare(i) {
        return <Square key={"square_" + i} value={this.props.squares[i]} onClick={() => this.props.onClick(i)} />
    }
    render() {
        return (
            <div>
                { this.renderBoard(this.props.size) }
            </div>
        );
    }
}

class Game extends React.Component {
    constructor() {
        super();
        this.size = 3;
        this.state = {
            stepNumber: 0,
            history: [{
                squares: Array(9).fill(null),
                order: null,
            }],
            xIsNext: true,
            isHistory: false,
        };
    }
    handleClick(i) {
        const history = this.state.history;
        const current = history[history.length - 1];
        const squares = current.squares.slice();
        if (calculateWinner(squares) || squares[i]) {
            return;
        }
        squares[i] = this.state.xIsNext ? 'X' : 'O';
        this.setState({
            stepNumber: history.length,
            history: history.concat([{
                squares: squares,
                order: history.length,
            }]),
            xIsNext: !this.state.xIsNext,
            isHistory: false,
            order: 'asc',
        });
    }
    handleOrder() {
        const sort = this.state.order === 'desc' ?
            (a, b) => a.order - b.order: // to asc
            (a, b) => b.order - a.order; // to desc
        const history = this.state.history.sort(sort);
        this.setState({
            order: this.state.order === 'desc' ? 'asc' : 'desc',
            history: history
        });
    }
    jumpTo(step) {
        this.setState({
            stepNumber: step,
            xIsNext: (step % 2) ? false : true,
            isHistory: true,
        });
    }
    render() {
        const history = this.state.history;
        const current = history[this.state.stepNumber];
        const winner = calculateWinner(current.squares);

        let status;
        if (winner) {
            status = 'Winner: ' + winner;
        } else {
            status = 'Next player: ' + (this.state.xIsNext ? 'X' : 'O');
        }
        const moves = history.map((el, index) => {
            const axis = {
                x: index % this.size,
                y: Math.floor(index / this.size) + 1,
            };
            const bold = index === this.state.stepNumber && this.state.isHistory ? "bold" : "";
            const desc = el.order ?
            `Move #${el.order} : (x: ${axis.x}, y: ${axis.y}) `:
            'Game start';

            return (
                <li key={"history_" + index} className={bold}>
                    <a href="#" onClick={() => this.jumpTo(index)}>{desc}</a>
                </li>
            );
        });
        return (
            <div className="game">
                <div className="game-board">
                    <Board size={3}
                           squares={current.squares}
                           onClick={(i) => this.handleClick(i)}
                    />
                </div>
                <div className="game-info">
                    <button onClick={() => this.handleOrder()}>↑↓</button>
                    <div>{ status }</div>
                    <ol>{ moves }</ol>
                </div>
            </div>
        );
    }
}

// ========================================

ReactDOM.render(
    <Game />,
    document.getElementById('container')
);

function calculateWinner(squares) {
    const lines = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
        const [a, b, c] = lines[i];
        if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
            return squares[a];
        }
    }
    return null;
}
