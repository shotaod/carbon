const host = 'http://localhost:7003';
const mock = 'http://localhost:7004';
export default function (env = 'development') {
  const isMock = env.endsWith('mock');
  return {
    host: isMock ? mock : host,
    env: isMock ? 'mock' : 'development',
  };
}
