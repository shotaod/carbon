export const keyValueIdentity = (key, value) => ({ [key]: value });

export const extractToken = (status) => {
  const { accessToken, tokenType } = status.auth.token;
  return {
    token: accessToken,
    tokenType,
  };
};

export default {
  keyValueIdentity,
  extractToken,
};
