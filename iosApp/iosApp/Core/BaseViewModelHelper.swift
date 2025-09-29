import Foundation
import Combine
import shared

@MainActor
class BaseViewModelHelper<State: CoreReducerViewState, Event: CoreReducerViewEvent, Effect: CoreReducerViewEffect>: ObservableObject {
    private var stateTask: Task<Void, Never>?
    private var effectTask: Task<Void, Never>?

    @Published private(set) var currentState: State
    @Published private(set) var latestEffect: Effect?

    let viewModel: CoreBaseViewModel<State, Event, Effect>

    init(viewModel: CoreBaseViewModel<State, Event, Effect>, initialState: State) {
        self.viewModel = viewModel
        self.currentState = initialState
        startObserving(stateFlow: AnyAsyncSequence(viewModel.state), effectFlow: AnyAsyncSequence(viewModel.effect))
    }

    /// Starts observing the state and optional effect streams
    private func startObserving(
        stateFlow: AnyAsyncSequence<State>,
        effectFlow: AnyAsyncSequence<Effect>? = nil
    ) {
        stateTask?.cancel()
        effectTask?.cancel()
        
        guard stateTask == nil, effectTask == nil else {
            return
        }

        // Observe state
        stateTask = Task<Void, Never> {
            do {
                for try await state in stateFlow {
                    guard !Task.isCancelled else {
                        break
                    }
                    currentState = state
                }
            } catch {
                print("Error in state flow: \(error)")
            }
        }

        // Observe effects (if present)
        if let effectFlow = effectFlow {
            effectTask = Task<Void, Never> {
                do {
                    for try await effect in effectFlow {
                        guard !Task.isCancelled else {
                            break
                        }
                        latestEffect = effect
                        handleEffect(effect)
                    }
                } catch {
                    print("Error in effect flow: \(error)")
                }
            }
        }
    }

    /// Hook for subclasses to react to one-off effects
    func handleEffect(_ effect: Effect) {
        // default no-op
    }

    func handleEvent(event: Event) {
        viewModel.handleEvent(event: event)
    }

    /// Stops observing
    deinit {
        stateTask?.cancel()
        effectTask?.cancel()
        stateTask = nil
        effectTask = nil
    }
}


struct AnyAsyncSequence<Element>: AsyncSequence {
    typealias AsyncIterator = AnyAsyncIterator<Element>

    private let _makeAsyncIterator: () -> AsyncIterator

    init<S: AsyncSequence>(_ sequence: S) where S.Element == Element {
        _makeAsyncIterator = {
            AnyAsyncIterator(sequence.makeAsyncIterator())
        }
    }

    func makeAsyncIterator() -> AsyncIterator {
        _makeAsyncIterator()
    }
}

struct AnyAsyncIterator<Element>: AsyncIteratorProtocol {
    private let _next: () async throws -> Element?

    init<I: AsyncIteratorProtocol>(_ iterator: I) where I.Element == Element {
        var iterator = iterator
        _next = {
            try await iterator.next()
        }
    }

    func next() async throws -> Element? {
        try await _next()
    }
}
