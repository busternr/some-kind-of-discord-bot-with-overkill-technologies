interface SessionState {
    sessionId: string
}

interface SessionModel {
    state: SessionState;
    reducers: typeof reducers;
    effects: typeof effects;
    selectors: typeof selectors;
}

const reducers = {
    update: (state: SessionState, payload: SessionState) => ({ ...state, ...payload }),
    setSessionId: (state: SessionState, payload: string) => ({ ...state, sessionId: payload })
};
const effects = {};

const selectors = {};

const session: SessionModel = {
    state: {
        sessionId: ''
    },
    reducers,
    effects,
    selectors
};

export default session;