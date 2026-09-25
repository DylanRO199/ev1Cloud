export function decodeJwt(token: string): Record<string, unknown> {
  const payload = token.split('.')[1];
  if (!payload) return {};

  const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
  const decoded = decodeURIComponent(
    atob(normalized)
      .split('')
      .map(c => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
      .join('')
  );

  return JSON.parse(decoded) as Record<string, unknown>;
}
